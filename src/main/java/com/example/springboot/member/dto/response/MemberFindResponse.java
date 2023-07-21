package com.example.springboot.member.dto.response;

import com.example.springboot.member.domain.entity.Member;
import com.example.springboot.todo.domain.entity.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberFindResponse {

    private Long id;

    private String email;

    private String name;

    private int age;

    private List<MemberOwnedTodosResponse> todos = new ArrayList<>();

    public MemberFindResponse(final Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.age = member.getAge();
        this.todos = member.getTodos()
                .stream()
                .map(MemberOwnedTodosResponse::new)
                .toList();
    }

    @Getter
    private static class MemberOwnedTodosResponse {

        private final Long id;

        private final String title;

        private final String content;

        private final Boolean isDone;

        private final Long likeCount;

        public MemberOwnedTodosResponse(Todo todo) {
            this.id = todo.getId();
            this.title = todo.getTitle();
            this.content = todo.getContent();
            this.isDone = todo.getIsDone();
            this.likeCount = todo.getLikeCount();
        }
    }

}
