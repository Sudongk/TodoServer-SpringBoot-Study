package com.example.springboot.todo.dto.response;

import com.example.springboot.member.domain.entity.Member;
import com.example.springboot.todo.domain.entity.Todo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoResponse {

    private Long id;

    private String title;

    private Boolean isDone;

    private Long likeCount;

    private Long memberId;

    private String memberName;

    @QueryProjection
    public TodoResponse(final Todo todo) {
        Member member = todo.getMember();
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.isDone = todo.getIsDone();
        this.likeCount = todo.getLikeCount();
        this.memberId = member.getId();
        this.memberName = member.getName();
    }
}
