package com.example.springboot.member.service;

import com.example.springboot.common.jwt.JwtProvider;
import com.example.springboot.member.domain.entity.Member;
import com.example.springboot.member.domain.entity.MemberLogin;
import com.example.springboot.member.dto.request.MemberLoginRequest;
import com.example.springboot.member.dto.request.MemberSignUpRequest;
import com.example.springboot.member.dto.response.MemberLoginResponse;
import com.example.springboot.member.dto.response.MemberSignUpResponse;
import com.example.springboot.member.dto.response.MembersResponse;
import com.example.springboot.member.exception.DuplicateEmailException;
import com.example.springboot.member.exception.PasswordInValidException;
import com.example.springboot.member.exception.MemberNotFoundException;
import com.example.springboot.member.repository.MemberLoginRepository;
import com.example.springboot.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberLoginRepository memberLoginRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, MemberLoginRepository memberLoginRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.memberLoginRepository = memberLoginRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public MemberSignUpResponse saveMember(MemberSignUpRequest request) {
        checkDuplicateEmail(request.getEmail());

        Member member = Member.builder()
                .password(request.getPassword())
                .email(request.getEmail())
                .name(request.getName())
                .age(Integer.parseInt(request.getAge()))
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberSignUpResponse.map(savedMember);
    }

    @Transactional
    public MemberLoginResponse loginMember(MemberLoginRequest request) {
        Member member = getMemberByEmail(request);

        validatePassword(request, member);

        renewalMemberLogin(member);

        String token = jwtProvider.generateToken(member);

        return MemberLoginResponse.map(member, token);
    }

    public MembersResponse findAllMember(Pageable pageable) {
        Page<Member> allMember = memberRepository.findAllMembersWithTodosAndLikes(pageable);
        return MembersResponse.map(allMember);
    }

    private void deleteExistingMemberLogin(Member member) {
        memberLoginRepository.findByMemberId(member.getId())
                .ifPresent(
                        memberLogin -> memberLoginRepository.deleteById(memberLogin.getId())
                );
    }

    private Member getMemberByEmail(MemberLoginRequest request) {
        return memberRepository.findByEmail(request.getEmail())
                .orElseThrow(MemberNotFoundException::new);
    }

    private void renewalMemberLogin(Member member) {
        deleteExistingMemberLogin(member);
        memberLoginRepository.save(new MemberLogin(member));
    }

    private static void validatePassword(MemberLoginRequest request, Member member) {
        if (!member.hasSamePassword(request.getPassword())) {
            throw new PasswordInValidException();
        }
    }

    private void checkDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }
}
