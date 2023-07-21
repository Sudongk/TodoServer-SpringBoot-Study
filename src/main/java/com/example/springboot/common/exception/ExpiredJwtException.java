package com.example.springboot.common.exception;

import org.springframework.http.HttpStatus;

public class ExpiredJwtException extends ApplicationException {
    public ExpiredJwtException() {
        super(HttpStatus.UNAUTHORIZED);
    }
}
