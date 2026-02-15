package com.arimar.gwent.communication.invoker;

import com.arimar.gwent.common.exception.ErrorDTO;
import com.arimar.gwent.common.response.GenericResponseDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class GenericRestInvokerImpl implements GenericRestInvoker {

  private final RestClient restClient;
  private final ObjectMapper objectMapper;
  private static final int MAX_LOG_BODY_CHARS = 3000;


  public GenericRestInvokerImpl(RestClient restClient, ObjectMapper objectMapper) {
    this.restClient = restClient;
    this.objectMapper = objectMapper;
  }
  @Override
  public <T> ServiceCallResponse<T> exchange(
          HttpMethod method,
          String url,
          Object body,
          Map<String, String> headers,
          ParameterizedTypeReference<GenericResponseDTO<T>> okType
  ) {
    Objects.requireNonNull(method, "method is required");
    Objects.requireNonNull(url, "url is required");
    Objects.requireNonNull(okType, "okType is required");

    final long t0 = System.nanoTime();

    Map<String, String> safeHeaders = (headers == null) ? Map.of() : headers;
    String correlationId = safeHeaders.getOrDefault("X-Correlation-Id",
            safeHeaders.getOrDefault("x-correlation-id", null));

    log.info("HTTP START method={} url={} correlationId={}",
            method, url, correlationId);

    try {
      var spec = restClient
              .method(method)
              .uri(url)
              .headers(h -> {
                if (!safeHeaders.containsKey("Accept")) {
                  h.set("Accept", "application/json");
                }

                safeHeaders.forEach((k, v) -> {
                  if (k != null && !k.isBlank() && v != null) {
                    h.set(k, v);
                  }
                });

                boolean hasBody = body != null && allowsBody(method);
                if (hasBody && !safeHeaders.containsKey("Content-Type")) {
                  h.set("Content-Type", "application/json");
                }
              });

      if (body != null && allowsBody(method)) {
        spec = spec.body(body);
      }

      return spec.exchange((request, response) -> {

        HttpStatusCode status = response.getStatusCode();
        long ms = (System.nanoTime() - t0) / 1_000_000;

        String respCorrelation =
                response.getHeaders().getFirst("X-Correlation-Id");

        if (status == HttpStatus.NO_CONTENT) {
          log.info("HTTP END method={} url={} status={} ms={} correlationId={} respCorrelationId={}",
                  method, url, status.value(), ms, correlationId, respCorrelation);
          return new ServiceCallResponse<>(status, null, null);
        }

        String rawBody = readBodyAsString(response);

        // Log general de respuesta
        log.info("HTTP END method={} url={} status={} ms={} correlationId={} respCorrelationId={}",
                method, url, status.value(), ms, correlationId, respCorrelation);

        if (status.is2xxSuccessful()) {
          if (rawBody == null || rawBody.isBlank()) {
            return new ServiceCallResponse<>(status, null, null);
          }
          GenericResponseDTO<T> ok = readOk(rawBody, okType);
          return new ServiceCallResponse<>(status, ok, null);
        }

        //log detallado solo para errores
        log.warn("HTTP ERROR method={} url={} status={} ms={} correlationId={} body={}",
                method, url, status.value(), ms, correlationId, truncate(rawBody));

        if (status.is3xxRedirection()) {
          String location = response.getHeaders().getFirst("Location");
          ErrorDTO err = buildError(
                  status,
                  "REDIRECT",
                  "Redirect to " + (location == null ? "<none>" : location),
                  url
          );
          return new ServiceCallResponse<>(status, null, err);
        }

        ErrorDTO err = tryParseError(rawBody, status, url);
        return new ServiceCallResponse<>(status, null, err);
      });

    } catch (Exception ex) {
      long ms = (System.nanoTime() - t0) / 1_000_000;

      HttpStatusCode pseudo = HttpStatus.SERVICE_UNAVAILABLE;

      log.error("HTTP TRANSPORT ERROR method={} url={} ms={} error={}",
              method, url, ms, ex.toString());

      ErrorDTO err = buildError(
              pseudo,
              "TRANSPORT_ERROR",
              safeMessage(ex),
              url
      );

      return new ServiceCallResponse<>(pseudo, null, err);
    }
  }

  private static boolean allowsBody(HttpMethod method) {
    return method == HttpMethod.POST
            || method == HttpMethod.PUT
            || method == HttpMethod.PATCH;
  }

  private static String safeMessage(Throwable ex) {
    String msg = ex.getMessage();
    return (msg == null || msg.isBlank()) ? "<no-message>" : msg;
  }

  private static ErrorDTO buildError(HttpStatusCode status, String code, String message, String url) {
    return ErrorDTO.builder()
            .status(status)
            .type(code)
            .message(message)
            .path(url)
            .build();
  }

  private String readBodyAsString(org.springframework.http.client.ClientHttpResponse response) throws IOException {
    return StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
  }

  private <T> GenericResponseDTO<T> readOk(String rawBody, ParameterizedTypeReference<GenericResponseDTO<T>> okType) {
    try {
      JavaType javaType = objectMapper.getTypeFactory().constructType(okType.getType());
      return objectMapper.readValue(rawBody, javaType);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize OK response to " + okType.getType(), e);
    }
  }

  private ErrorDTO tryParseError(String rawBody, HttpStatusCode httpStatus, String url) {
    if (rawBody != null && !rawBody.isBlank()) {
      // Caso 1: upstream devuelve GenericResponseDTO<ErrorDTO>
      try {
        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(GenericResponseDTO.class, ErrorDTO.class);
        GenericResponseDTO<ErrorDTO> wrapper = objectMapper.readValue(rawBody, type);
        if (wrapper != null && wrapper.getData() != null) {
          return wrapper.getData();
        }
      } catch (Exception ignored) {
        this.defaultErrorBuilder(url, rawBody);
      }

      // Caso 2: upstream devuelve ErrorDTO directo
      try {
        return objectMapper.readValue(rawBody, ErrorDTO.class);
      } catch (Exception ignored) {
        return this.defaultErrorBuilder(url, rawBody);
      }
    }
      return defaultErrorBuilder(url, null);
  }
  private ErrorDTO defaultErrorBuilder(String url, String rawBody){
    if(rawBody == null){
      return ErrorDTO.builder()
              .serviceOrigin(null)
              .status(HttpStatus.INTERNAL_SERVER_ERROR)
              .path(url)
              .message("No se pudo leer la respuesta de error porque es null")
              .type("ERROR_RESPONSE_NULL")
              .body(null)
              .build();
    }
    return ErrorDTO.builder()
            .serviceOrigin(null)
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .path(url)
            .message("No se pudo leer la respuesta de error")
            .type("ERROR_RESPONSE")
            .body(rawBody)
            .build();
  }

  private static String truncate(String body) {
    if (body == null) return null;
    if (body.length() <= MAX_LOG_BODY_CHARS) return body;
    return body.substring(0, MAX_LOG_BODY_CHARS) + "...(truncated)";
  }

}
