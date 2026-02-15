package com.arimar.gwent.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Builder
@Data
@AllArgsConstructor
public class ErrorDTO {
    private String serviceOrigin;
    private HttpStatusCode status;
    private String path;
    private String message;
    private String type;
    private String body;
}
