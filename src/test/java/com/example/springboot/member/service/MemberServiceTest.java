package com.example.springboot.member.service;

import com.example.springboot.ServiceTestConfig;
import com.example.springboot.member.domain.entity.Member;
import com.example.springboot.member.dto.request.MemberLoginRequest;
import com.example.springboot.member.dto.request.MemberSignUpRequest;
import com.example.springboot.member.exception.DuplicateEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
@DisplayName("사용자 서비스 테스트")
class MemberServiceTest extends ServiceTestConfig {

    @InjectMocks
    private MemberService memberService;

    private static final Long MEMBER_ID = 1L;
    private static final String EMAIL = "aaa@aaa.com";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final int AGE = 20;
    private static final String TOKEN = "token";


    private MemberSignUpRequest getMemberSignUpRequest() {
        return new MemberSignUpRequest(EMAIL, PASSWORD, NAME, String.valueOf(AGE));
    }

    private MemberLoginRequest getMemberLoginRequest() {
        return new MemberLoginRequest(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("회원가입 성공")
    void saveMember() {
        // given
        MemberSignUpRequest request = getMemberSignUpRequest();

        Member member = Member.builder()
                .id(MEMBER_ID)
                .password(request.getPassword())
                .email(request.getEmail())
                .name(request.getName())
                .age(Integer.parseInt(request.getAge()))
                .build();

        given(memberRepository.existsByEmail(request.getEmail()))
                .willReturn(false);

        given(memberRepository.save(any(Member.class)))
                .willReturn(member);

        // when
        memberService.saveMember(request);

        // then
        then(memberRepository).should(times(1))
                .existsByEmail(any());

        then(memberRepository).should(times(1))
                .save(any());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void saveMemberFailWhenDuplicateEmail() {
        // given
        MemberSignUpRequest request = getMemberSignUpRequest();

        Member member = Member.builder()
                .id(MEMBER_ID)
                .password(request.getPassword())
                .email(request.getEmail())
                .name(request.getName())
                .age(Integer.parseInt(request.getAge()))
                .build();

        // when
        given(memberRepository.existsByEmail(request.getEmail()))
                .willReturn(true);

        // then
        assertThatThrownBy(() -> memberService.saveMember(request))
                .isInstanceOf(DuplicateEmailException.class);

        then(memberRepository).should(times(1))
                .existsByEmail(any());

        then(memberRepository).should(never())
                .save(any());
    }
}