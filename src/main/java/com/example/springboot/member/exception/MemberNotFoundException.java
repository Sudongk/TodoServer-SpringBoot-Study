package com.example.springboot.member.exception;

import com.example.springboot.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends ApplicationException {
    public MemberNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
