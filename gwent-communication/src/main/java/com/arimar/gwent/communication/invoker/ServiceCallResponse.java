package com.arimar.gwent.communication.invoker;

import com.arimar.gwent.common.exception.ErrorDTO;
import com.arimar.gwent.common.response.GenericResponseDTO;
import org.springframework.http.HttpStatusCode;

public record ServiceCallResponse<T>(
    HttpStatusCode httpStatus,
    GenericResponseDTO<T> ok,
    ErrorDTO error
) {
  public boolean isOk() { return ok != null; }
}
