package com.example.springboot.todo.dto.response;

import com.example.springboot.todo.domain.entity.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoCreateResponse {

    private Long id;

    public TodoCreateResponse(Long id) {
        this.id = id;
    }

    public static TodoCreateResponse map(final Todo todo) {
        return new TodoCreateResponse(todo.getId());
    }
}
