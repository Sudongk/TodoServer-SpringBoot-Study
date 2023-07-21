package com.example.springboot.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

    public ApplicationException(HttpStatus status) {
        ExceptionType exceptionType = ExceptionType.of(this.getClass());
        this.status = status;
        this.errorCode = exceptionType.getErrorCode();
        this.errorMessage = exceptionType.getErrorMessage();
    }
}
