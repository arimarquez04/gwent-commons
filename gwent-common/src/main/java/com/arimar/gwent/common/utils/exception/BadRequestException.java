package com.arimar.gwent.common.utils.exception;

import org.springframework.validation.BindingResult;

public class BadRequestException extends Exception {
    private final transient BindingResult bindingResult;

    public BadRequestException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

}
