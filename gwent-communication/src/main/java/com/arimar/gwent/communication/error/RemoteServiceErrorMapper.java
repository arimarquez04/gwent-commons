package com.arimar.gwent.communication.error;

import com.arimar.gwent.common.exception.ErrorDTO;
import com.arimar.gwent.common.exception.InternalService4xxErrorException;
import com.arimar.gwent.common.exception.InternalService5xxErrorException;
import com.arimar.gwent.common.exception.RemoteServiceCommunicationException;
import org.springframework.http.HttpStatusCode;

import java.util.Objects;

public interface RemoteServiceErrorMapper {

    RuntimeException toException(
            String serviceName,
            String url,
            HttpStatusCode statusCode,
            ErrorDTO error
    );
    String buildMessage(
            String service,
            String url,
            int status,
            ErrorDTO error
    );
}
