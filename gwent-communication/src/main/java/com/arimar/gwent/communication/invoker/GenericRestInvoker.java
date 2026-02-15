package com.arimar.gwent.communication.invoker;

import com.arimar.gwent.common.response.GenericResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.Map;

public interface GenericRestInvoker {
  <T> ServiceCallResponse<T> exchange(
      HttpMethod method,
      String url,
      Object body, // puede ser null
      Map<String, String> headers,
      ParameterizedTypeReference<GenericResponseDTO<T>> okType
  );
}
