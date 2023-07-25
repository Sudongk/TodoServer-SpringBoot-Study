package com.example.springboot.member.repository;

import com.example.springboot.member.dto.request.MemberCondition;
import com.example.springboot.member.dto.response.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMemberRepository {
    Page<MemberResponse> findAllMemberByCondition(MemberCondition memberCondition, Long memberId, Pageable pageable);
}
