package com.example.springboot.todo.dto.response;

import com.example.springboot.common.page.PageInfo;
import com.example.springboot.todo.domain.entity.Todo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class TodosResponse {

    private List<TodoResponse> todos;

    private PageInfo pageInfo;

    public TodosResponse(List<TodoResponse> todos, PageInfo pageInfo) {
        this.todos = todos;
        this.pageInfo = pageInfo;
    }

    public static TodosResponse map(final Page<Todo> todoPage) {
        List<TodoResponse> responses = todoPage.map(TodoResponse::new).getContent();
        return new TodosResponse(responses, PageInfo.of(todoPage));
    }
}
