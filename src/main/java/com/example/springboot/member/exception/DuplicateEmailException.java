package com.example.springboot.member.exception;

import com.example.springboot.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends ApplicationException {
    public DuplicateEmailException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
