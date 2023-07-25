package com.example.springboot.member.dto.request;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class MemberCondition {

    private String name;
    private Integer ageLoe;
    private Integer ageGoe;

}
