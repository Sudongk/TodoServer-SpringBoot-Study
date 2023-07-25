package com.example.springboot.todo.repository;

import com.example.springboot.todo.domain.entity.Todo;
import com.example.springboot.todo.dto.request.TodoCondition;
import com.example.springboot.todo.dto.response.TodoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomTodoRepository {
    Page<Todo> findAllByCondition(TodoCondition todoCondition, Pageable pageable);

    Page<TodoResponse> findAllByConditionV2(TodoCondition todoCondition, Pageable pageable);
}
