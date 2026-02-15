package com.arimar.gwent.communication.error;

import com.arimar.gwent.common.exception.ErrorDTO;
import com.arimar.gwent.common.exception.InternalService4xxErrorException;
import com.arimar.gwent.common.exception.InternalService5xxErrorException;
import com.arimar.gwent.common.exception.RemoteServiceCommunicationException;
import org.springframework.http.HttpStatusCode;

import java.util.Objects;

public final class RemoteServiceErrorMapperImpl implements RemoteServiceErrorMapper {

    /**
     * Traduce un error remoto a una excepción del dominio.
     *
     * @param serviceName nombre lógico del servicio remoto (ej: auth-service)
     * @param url endpoint invocado
     * @param statusCode status HTTP recibido
     * @param error errorDTO remoto (puede ser null)
     */
    @Override
    public RuntimeException toException(
            String serviceName,
            String url,
            HttpStatusCode statusCode,
            ErrorDTO error
    ) {

        Objects.requireNonNull(serviceName, "serviceName is required");
        Objects.requireNonNull(url, "url is required");
        Objects.requireNonNull(statusCode, "status is required");

        int code = statusCode.value();

        //String message = buildMessage(serviceName, url, code, error);

        // 🔹 4xx → error funcional / negocio
        if (code >= 400 && code < 500) {
            return new InternalService4xxErrorException(
                    serviceName,
                    statusCode,
                    error
            );
        }

        // 🔹 5xx → error técnico remoto
        if (code >= 500) {
            return new InternalService5xxErrorException(
                    serviceName,
                    statusCode,
                    error
            );
        }

        // transporte / casos inesperados
        return new RemoteServiceCommunicationException(
                serviceName,
                url,
                statusCode,
                error
        );
    }

    /**
     * Construye un mensaje consistente para logs y debugging.
     */
    @Override
    public String buildMessage(
            String service,
            String url,
            int status,
            ErrorDTO error
    ) {
        String remoteMsg = (error == null || error.getMessage() == null)
                ? "<no-remote-message>"
                : error.getMessage();

        String remoteCode = (error == null || error.getStatus() == null)
                ? "N/A"
                : error.getStatus().toString();

        return String.format(
                "Remote call failed → service=%s status=%d url=%s remoteCode=%s message=%s",
                service,
                status,
                url,
                remoteCode,
                remoteMsg
        );
    }
}
