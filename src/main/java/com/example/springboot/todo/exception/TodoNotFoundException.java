package com.example.springboot.todo.exception;

import com.example.springboot.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TodoNotFoundException extends ApplicationException {
    public TodoNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
