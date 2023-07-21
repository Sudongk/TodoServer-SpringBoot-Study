package com.example.springboot.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.springboot.common.exception.ValidatedMessage.*;

@Getter
@NoArgsConstructor
public class TodoCreateRequest {

    @NotBlank(message = EMPTY_MESSAGE)
    @Size(max = 255, message = TITLE_MESSAGE)
    private String title;

    @NotBlank(message = EMPTY_MESSAGE)
    @Size(max = 255, message = CONTENT_MESSAGE)
    private String content;

    private Boolean isDone;

    public TodoCreateRequest(final String title, final String content, final Boolean isDone) {
        this.title = title;
        this.content = content;
        this.isDone = isDone;
    }
}
