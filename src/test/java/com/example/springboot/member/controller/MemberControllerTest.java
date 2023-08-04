package com.example.springboot.member.controller;

import com.example.springboot.ControllerTestConfig;
import com.example.springboot.member.dto.request.MemberSignUpRequest;
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

    private final Long memberId = 1L;
    private final String email = "aaa@aaa.com";
    private final String password = "12345";
    private final String name = "aaa";
    private final String age = "20";

    private MemberSignUpRequest getMemberSignUpRequest() {
        return new MemberSignUpRequest(email, password, name, age);
    }

    private MemberSignUpResponse getMemberSignUpResponse() {
        return new MemberSignUpResponse(memberId);
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
                .andExpect(jsonPath("$.id", equalTo(memberId.intValue())))
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
                new MemberSignUpRequest("aa88sd@naver.com", "password", "name", "20");

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
}