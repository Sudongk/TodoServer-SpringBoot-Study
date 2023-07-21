package com.example.springboot.common.exception;

import org.springframework.http.HttpStatus;

public class InValidJwtException extends ApplicationException {
    public InValidJwtException() {
        super(HttpStatus.UNAUTHORIZED);
    }
}
