package com.example.springboot.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.example.springboot.common.exception.ValidatedMessage.*;

@Getter
@NoArgsConstructor
public class MemberLoginRequest {

    @NotBlank(message = EMPTY_MESSAGE)
    @Email(message = EMAIL_MESSAGE)
    private String email;

    @NotBlank(message = EMPTY_MESSAGE)
    @Length(max = 30)
    private String password;

    public MemberLoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
