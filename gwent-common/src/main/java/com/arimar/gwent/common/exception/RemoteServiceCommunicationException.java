package com.arimar.gwent.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class RemoteServiceCommunicationException extends RuntimeException {

    private final String service;
    private final String url;
    private final ErrorDTO error;
    private final HttpStatusCode code;

    public RemoteServiceCommunicationException(
            String service,
            String url,
            HttpStatusCode code,
            ErrorDTO error
    ) {
        super(error.getMessage());
        this.service = service;
        this.url = url;
        this.error = error;
        this.code = code;
    }

}
