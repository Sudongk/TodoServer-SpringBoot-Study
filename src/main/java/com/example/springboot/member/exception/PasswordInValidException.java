package com.example.springboot.member.exception;

import com.example.springboot.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PasswordInValidException extends ApplicationException {
    public PasswordInValidException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
