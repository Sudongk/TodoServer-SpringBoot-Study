package com.example.springboot.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TodoCondition {

    private String title;
    private String content;
    private Boolean isDone;
    private Integer likeGoe;
    private Integer likeLoe;

}
