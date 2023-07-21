package com.example.springboot.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.springboot.common.exception.ValidatedMessage.*;

@Getter
@NoArgsConstructor
public class MemberSignUpRequest {

    @NotBlank(message = EMPTY_MESSAGE)
    @Email(message = EMAIL_MESSAGE)
    private String email;

    @NotBlank(message = EMPTY_MESSAGE)
    @Size(max = 10, message = PASSWORD_MESSAGE)
    private String password;

    @NotBlank(message = EMAIL_MESSAGE)
    @Size(max = 10, message = NAME_MESSAGE)
    private String name;

    @NotBlank(message = EMPTY_MESSAGE)
    @Pattern(regexp = AGE_REGEX, message = AGE_MESSAGE)
    private String age;

    public MemberSignUpRequest(final String email, final String password, final String name, final String age) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
    }
}
