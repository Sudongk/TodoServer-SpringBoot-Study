package com.example.springboot.member.controller;

import com.example.springboot.ControllerTestConfig;
import com.example.springboot.member.dto.request.MemberLoginRequest;
import com.example.springboot.member.dto.request.MemberSignUpRequest;
import com.example.springboot.member.dto.response.MemberLoginResponse;
import com.example.springboot.member.dto.response.MemberSignUpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest extends ControllerTestConfig {

    private static final Long MEMBER_ID = 1L;
    private static final String EMAIL = "aaa@aaa.com";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final int AGE = 20;
    private static final String TOKEN = "token";

    private MemberSignUpRequest getMemberSignUpRequest() {
        return new MemberSignUpRequest(EMAIL, PASSWORD, NAME, String.valueOf(AGE));
    }

    private MemberSignUpResponse getMemberSignUpResponse() {
        return new MemberSignUpResponse(MEMBER_ID);
    }

    private MemberLoginRequest getMemberLoginRequest() {
        return new MemberLoginRequest(EMAIL, PASSWORD);
    }

    private MemberLoginResponse getMemberLoginResponse() {
        return new MemberLoginResponse(MEMBER_ID, NAME, AGE, TOKEN);
    }

    @DisplayName("회원 가입 성공 - 가입된 회원의 pk값 반환")
    @Test
    void memberJoinSuccess() throws Exception {
        // given
        MemberSignUpRequest memberSignUpRequest = getMemberSignUpRequest();
        MemberSignUpResponse memberSignUpResponse = getMemberSignUpResponse();

        given(memberService.saveMember(any()))
                .willReturn(memberSignUpResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(MEMBER_ID.intValue())))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    MemberSignUpResponse actualResponse = objectMapper.readValue(content, MemberSignUpResponse.class);
                    assertThat(actualResponse).isEqualTo(memberSignUpResponse);
                });

        then(memberService).should().saveMember(any());
    }

    @DisplayName("회원 가입 실패 - 이메일 형식이 잘못된 경우")
    @Test
    void memberJoinFailWhenInvalidEmail() throws Exception {
        // given
        MemberSignUpRequest memberSignUpRequest =
                new MemberSignUpRequest("aa@", "password", "name", "20");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("Email")))
                .andExpect(jsonPath("$.message", equalTo("email : INVALID EMAIL! (request value: aa@)")))
        ;

        then(memberService).should(never()).saveMember(any());
    }

    @DisplayName("회원 가입 실패 - 이메일이 공백이거나 null인 경우")
    @Test
    void memberJoinFailWhenEmailIsEmpty() throws Exception {
        // given
        MemberSignUpRequest memberSignUpRequest =
                new MemberSignUpRequest("", "password", "name", "20");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("NotBlank")))
                .andExpect(jsonPath("$.message", equalTo("email : EMPTY! (request value: )")))
        ;

        then(memberService).should(never()).saveMember(any());
    }

    @DisplayName("회원 가입 실패 - 비밀번호가 공백이거나 null인 경우")
    @Test
    void memberJoinFailWhenPasswordIsEmpty() throws Exception {
        // given
        MemberSignUpRequest memberSignUpRequest =
                new MemberSignUpRequest("aa88sd@naver.com", "", "name", "20");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("NotBlank")))
                .andExpect(jsonPath("$.message", equalTo("password : EMPTY! (request value: )")))
        ;

        then(memberService).should(never()).saveMember(any());
    }

    @DisplayName("회원 가입 실패 - 비밀번호가 너무 긴 경우")
    @Test
    void memberJoinFailWhenPasswordIsTooLong() throws Exception {
        // given
        MemberSignUpRequest memberSignUpRequest =
                new MemberSignUpRequest("aa88sd@naver.com", "12345678910", "name", "20");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("Size")))
                .andExpect(jsonPath("$.message", equalTo("password : TOO LONG PASSWORD! (request value: 12345678910)")))
        ;

        then(memberService).should(never()).saveMember(any());
    }

    @DisplayName("회원 가입 실패 - 이름이 공백인 경우")
    @Test
    void memberJoinFailWhenNameIsEmpty() throws Exception {
        // given
        MemberSignUpRequest memberSignUpRequest =
                new MemberSignUpRequest("aa88sd@naver.com", "password", "", "20");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("NotBlank")))
                .andExpect(jsonPath("$.message", equalTo("name : INVALID EMAIL! (request value: )")))
        ;

        then(memberService).should(never()).saveMember(any());
    }

    @DisplayName("회원 가입 실패 - 이름이 너무 긴 경우")
    @Test
    void memberJoinFailWhenNameIsTooLong() throws Exception {
        // given
        MemberSignUpRequest memberSignUpRequest =
                new MemberSignUpRequest("aa88sd@naver.com", "password", "namenamename", "20");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("Size")))
                .andExpect(jsonPath("$.message", equalTo("name : TOO LONG NAME! (request value: namenamename)")))
        ;

        then(memberService).should(never()).saveMember(any());
    }

    @DisplayName("회원 가입 실패 - 나이가 공백인 경우")
    @Test
    void memberJoinFailWhenAgeIsEmpty() throws Exception {
        // given
        MemberSignUpRequest memberSignUpRequest =
                new MemberSignUpRequest("aa88sd@naver.com", "password", "name", "");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("Pattern")))
                .andExpect(jsonPath("$.message", equalTo("age : INVALID AGE! (request value: )")))
        ;

        then(memberService).should(never()).saveMember(any());
    }

    @DisplayName("회원 가입 실패 - 나이 형식에 맞지 않는 경우(숫자가 아니거나 2자리 초과인 경우)")
    @Test
    void memberJoinFailWhenAgeIsInvalid() throws Exception {
        // given
        MemberSignUpRequest memberSignUpRequest =
                new MemberSignUpRequest("aa88sd@naver.com", "password", "name", "112233");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("Pattern")))
                .andExpect(jsonPath("$.message", equalTo("age : INVALID AGE! (request value: 112233)")))
        ;

        then(memberService).should(never()).saveMember(any());
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() throws Exception {
        // given
        MemberLoginRequest memberLoginRequest = getMemberLoginRequest();

        MemberLoginResponse memberLoginResponse = getMemberLoginResponse();

        given(memberService.loginMember(any()))
                .willReturn(memberLoginResponse);

        given(jwtProvider.generateToken(any()))
                .willReturn(TOKEN);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(MEMBER_ID.intValue())))
                .andExpect(jsonPath("$.name", equalTo(NAME)))
                .andExpect(jsonPath("$.age", equalTo(AGE)))
                .andExpect(jsonPath("$.token", equalTo(TOKEN)))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    MemberLoginResponse actualResponse = objectMapper.readValue(content, MemberLoginResponse.class);
                    assertThat(actualResponse).isEqualTo(memberLoginResponse);
                });

        then(memberService).should().loginMember(any());
    }

    @Test
    @DisplayName("로그인 실패 - 이메일이 공백인 경우")
    void loginFailWhenEmailIsEmpty() throws Exception {
        // given
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("", PASSWORD);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("NotBlank")))
                .andExpect(jsonPath("$.message", equalTo("email : EMPTY! (request value: )")))
        ;
    }

    @Test
    @DisplayName("로그인 실패 - 이메일이 null인 경우")
    void loginFailWhenEmailIsNull() throws Exception {
        // given
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest(null, "password");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("NotBlank")))
                .andExpect(jsonPath("$.message", equalTo("email : EMPTY! (request value: null)")))
        ;
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 형식이 잘못된 경우")
    void loginFailWhenEmailIsInValid() throws Exception {
        // given
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("aa@", PASSWORD);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("Email")))
                .andExpect(jsonPath("$.message", equalTo("email : INVALID EMAIL! (request value: aa@)")))
        ;
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호가 공백인 경우")
    void loginFailWhenPasswordIsEmpty() throws Exception {
        // given
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest(EMAIL, "");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("NotBlank")))
                .andExpect(jsonPath("$.message", equalTo("password : EMPTY! (request value: )")))
        ;
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호가 null인 경우")
    void loginFailWhenPasswordIsNull() throws Exception {
        // given
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest(EMAIL, null);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("NotBlank")))
                .andExpect(jsonPath("$.message", equalTo("password : EMPTY! (request value: null)")))
        ;
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 길이가 너무 긴 경우")
    void loginFailWhenPasswordIsTooLong() throws Exception {
        // given
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest(EMAIL, "passwordpasswordpasswordpassword");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/members/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                        .characterEncoding("UTF-8")
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", equalTo("Length")))
                .andExpect(jsonPath("$.message", equalTo("password : length must be between 0 and 30 (request value: passwordpasswordpasswordpassword)")))
        ;
    }
}

