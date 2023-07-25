package com.example.springboot.member.dto.response;

import com.example.springboot.common.page.PageInfo;
import com.example.springboot.member.domain.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class MembersResponse {

    private List<MemberResponse> members;

    PageInfo pageInfo;

    private MembersResponse(List<MemberResponse> members, PageInfo pageInfo) {
        this.members = members;
        this.pageInfo = pageInfo;
    }

    public static MembersResponse mapMember(final Page<Member> memberPage) {
        List<MemberResponse> responses = memberPage.map(MemberResponse::new).getContent();
        return new MembersResponse(responses, PageInfo.of(memberPage));
    }

    public static MembersResponse mapMemberResponse(final Page<MemberResponse> memberResponsePage) {
        List<MemberResponse> responses = memberResponsePage.getContent();
        return new MembersResponse(responses, PageInfo.of(memberResponsePage));
    }

}
