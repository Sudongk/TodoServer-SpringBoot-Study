package com.example.springboot.common.exception;

import com.example.springboot.common.exception.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    private static final String INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT = "%s : %s (request value: %s)";

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e) {
        log.info(String.format("Application Exception!! type : %s", e.getClass().getSimpleName()));

        return ResponseEntity.status(e.getStatus())
                .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info(String.format("MethodArgumentNotValidException : %s", e));

        FieldError firstFieldError = e.getFieldErrors().get(0);
        String errorCode = firstFieldError.getCode();
        String errorMessage = String.format(INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT, firstFieldError.getField(),
                firstFieldError.getDefaultMessage(), firstFieldError.getRejectedValue());

        ErrorResponse errorResponse = new ErrorResponse(errorCode, errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({
            WeakKeyException.class,
            SignatureException.class
    })
    public ResponseEntity<ErrorResponse> weakKeyExceptionHandler(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ExceptionType.INVALID_JWT_EXCEPTION.getErrorCode(), ExceptionType.INVALID_JWT_EXCEPTION.getErrorMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> expiredJwtExceptionHandler(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ExceptionType.EXPIRED_JWT_EXCEPTION.getErrorCode(), ExceptionType.EXPIRED_JWT_EXCEPTION.getErrorMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ExceptionType.MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unknownException(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();

        log.error(String.format("Unknown Exception !! : %s\n" + "%s:%s:%s", e, stackTrace[0].getClassName(),
                stackTrace[0].getMethodName(), stackTrace[0].getLineNumber()), e);

        String errorCode = ExceptionType.UNKNOWN_EXCEPTION.getErrorCode();
        String message = ExceptionType.UNKNOWN_EXCEPTION.getErrorMessage();
        return ResponseEntity.internalServerError().body(new ErrorResponse(errorCode, message));
    }
}
