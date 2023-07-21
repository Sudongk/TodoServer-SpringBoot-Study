package com.example.springboot.member.dto.response;

import com.example.springboot.member.domain.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginResponse {

    private Long id;

    private String name;

    private int age;

    private String token;

    private MemberLoginResponse(final Long id, final String name, final int age, final String token) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.token = token;
    }

    public static MemberLoginResponse map(final Member member, final String token) {
        return new MemberLoginResponse(member.getId(), member.getName(), member.getAge(), token);
    }
}
