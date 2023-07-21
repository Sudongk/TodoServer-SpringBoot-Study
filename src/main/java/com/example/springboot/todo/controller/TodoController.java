package com.example.springboot.todo.controller;

import com.example.springboot.common.aop.CheckAuth;
import com.example.springboot.common.aop.MemberId;
import com.example.springboot.like.service.LikeService;
import com.example.springboot.todo.dto.request.TodoCreateRequest;
import com.example.springboot.todo.dto.request.TodoUpdateRequest;
import com.example.springboot.todo.dto.response.TodoCreateResponse;
import com.example.springboot.todo.dto.response.TodosResponse;
import com.example.springboot.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/todos")
@Validated
public class TodoController {

    private final TodoService todoService;
    private final LikeService likeService;

    public TodoController(TodoService todoService, LikeService likeService) {
        this.todoService = todoService;
        this.likeService = likeService;
    }

    @GetMapping("/content")
    public ResponseEntity<TodosResponse> findTodosContainsContent(
            @RequestParam(value = "content", defaultValue = "") String content,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "isDone", defaultValue = "null") Boolean isDone) {

        TodosResponse todosResponse = todoService.findTodosContainsContent(content, PageRequest.of(page, size), isDone);

        return ResponseEntity.ok(todosResponse);
    }

    @GetMapping("/title")
    public ResponseEntity<TodosResponse> findTodosContainsTitle(
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        TodosResponse todosResponse = todoService.findTodosContainsTitle(title, PageRequest.of(page, size));

        return ResponseEntity.ok(todosResponse);
    }

    @CheckAuth
    @PostMapping
    public ResponseEntity<TodoCreateResponse> create(
            @MemberId Long memberId,
            @RequestBody @Valid TodoCreateRequest todoCreateRequest) {

        TodoCreateResponse todoCreateResponse = todoService.createTodo(todoCreateRequest, memberId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(todoCreateResponse);
    }

    @CheckAuth
    @PutMapping("/{todoId}")
    public ResponseEntity<Void> update(
            @MemberId Long memberId,
            @PathVariable Long todoId,
            @RequestBody @Valid TodoUpdateRequest todoUpdateRequest) {

        todoService.updateTodo(todoUpdateRequest, memberId, todoId);

        return ResponseEntity
                .ok()
                .build();
    }

    @CheckAuth
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> delete(
            @MemberId Long memberId,
            @PathVariable Long todoId) {

        todoService.deleteTodo(memberId, todoId);

        return ResponseEntity
                .ok()
                .build();
    }

    @CheckAuth
    @PostMapping("/{todoId}/like")
    public ResponseEntity<Void> like(
            @MemberId Long memberId,
            @PathVariable Long todoId) {

        likeService.like(memberId, todoId);

        return ResponseEntity
                .ok()
                .build();
    }

    @CheckAuth
    @PutMapping("/{todoId}/check")
    public ResponseEntity<Void> checkIsDone(
            @MemberId Long memberId,
            @PathVariable("todoId") Long todoId) {

        todoService.updateIsDoneStatus(memberId, todoId);

        return ResponseEntity
                .ok()
                .build();
    }
}
