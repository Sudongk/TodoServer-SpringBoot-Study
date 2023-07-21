package com.example.springboot.member.dto.response;

import com.example.springboot.member.domain.entity.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
// 테스트를 위한 EqualsAndHashCode 오버라이드
@EqualsAndHashCode(of = "id")
public class MemberSignUpResponse {

    private Long id;

    public MemberSignUpResponse(final Long id) {
        this.id = id;
    }

    public static MemberSignUpResponse map(final Member member) {
        return new MemberSignUpResponse(member.getId());
    }
}
