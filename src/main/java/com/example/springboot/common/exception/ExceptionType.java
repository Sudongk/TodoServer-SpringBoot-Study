package com.example.springboot.common.exception;

import com.example.springboot.member.exception.DuplicateEmailException;
import com.example.springboot.member.exception.MemberNotFoundException;
import com.example.springboot.todo.exception.TodoNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ExceptionType {

    UNKNOWN_EXCEPTION("000", "알 수 없는 서버 에러"),

    MEMBER_NOT_FOUND_EXCEPTION("001", "존재하지 않는 회원", MemberNotFoundException.class),

    TODO_NOT_FOUND_EXCEPTION("002", "존재하지 않는 TODO", TodoNotFoundException.class),

    EMAIL_DUPLICATE_EXCEPTION("003", "이미 존재하는 이메일", DuplicateEmailException.class),

    UNAUTHORIZED_EXCEPTION("004", "권한 없음, 다시 로그인", UnAuthorizedException.class),

    EXPIRED_JWT_EXCEPTION("005", "기간이 만료된 토큰입니다.", ExpiredJwtException.class),
    INVALID_JWT_EXCEPTION("006", "유효하지 않은 토큰입니다.", InValidJwtException.class),

    MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION("007", "필요힌 파라미터가 존재하지 않습니다.")
    ;

    private final String errorCode;
    private final String errorMessage;
    private Class<? extends ApplicationException> type;

    ExceptionType(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ExceptionType of(Class<?> classType) {
        return Arrays.stream(values())
                .filter(eType -> Objects.nonNull(eType.type) && eType.type.equals(classType))
                .findFirst()
                .orElse(UNKNOWN_EXCEPTION);
    }
}
