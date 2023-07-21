package com.example.springboot.common.exception;

public class ValidatedMessage {
    public static final String EMPTY_MESSAGE = "EMPTY!";
    public static final String EMAIL_MESSAGE = "INVALID EMAIL!";
    public static final String PASSWORD_MESSAGE = "TOO LONG PASSWORD!";
    public static final String NAME_MESSAGE = "TOO LONG NAME!";
    public static final String AGE_MESSAGE = "INVALID AGE!";
    public static final String TITLE_MESSAGE = "TOO LONG TITLE!";
    public static final String CONTENT_MESSAGE = "TOO LONG CONTENT!";

    public static final String AGE_REGEX = "^[0-9]{1,2}$";
}
