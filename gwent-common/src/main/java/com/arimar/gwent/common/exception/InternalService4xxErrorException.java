package com.arimar.gwent.common.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class InternalService4xxErrorException extends RuntimeException {

    private final String serviceErrorOrigin;
    private final HttpStatusCode httpStatus;
    private final ErrorDTO error;

    public InternalService4xxErrorException(String serviceErrorOrigin, HttpStatusCode httpStatus, ErrorDTO error) {
        super("4xx from " + serviceErrorOrigin + " status=" + httpStatus.value());
        this.serviceErrorOrigin = serviceErrorOrigin;
        this.httpStatus = httpStatus;
        this.error = error;
    }
}
