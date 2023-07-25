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

    }
}