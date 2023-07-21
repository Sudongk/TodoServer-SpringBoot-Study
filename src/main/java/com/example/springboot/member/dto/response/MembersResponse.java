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

    private List<MemberFindResponse> members;

    PageInfo pageInfo;

    private MembersResponse(List<MemberFindResponse> members, PageInfo pageInfo) {
        this.members = members;
        this.pageInfo = pageInfo;
    }

    public static MembersResponse map(final Page<Member> memberPage) {
        List<MemberFindResponse> responses = memberPage.map(MemberFindResponse::new).getContent();
        return new MembersResponse(responses, PageInfo.of(memberPage));
    }

}
