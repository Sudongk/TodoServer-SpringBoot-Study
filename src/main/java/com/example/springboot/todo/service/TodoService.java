package com.example.springboot.todo.service;

import com.example.springboot.member.domain.entity.Member;
import com.example.springboot.member.exception.MemberNotFoundException;
import com.example.springboot.member.repository.MemberRepository;
import com.example.springboot.todo.domain.entity.Todo;
import com.example.springboot.todo.dto.request.TodoCreateRequest;
import com.example.springboot.todo.dto.request.TodoUpdateRequest;
import com.example.springboot.todo.dto.response.TodoCreateResponse;
import com.example.springboot.todo.dto.response.TodosResponse;
import com.example.springboot.todo.exception.TodoNotFoundException;
import com.example.springboot.todo.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    public TodoService(TodoRepository todoRepository, MemberRepository memberRepository) {
        this.todoRepository = todoRepository;
        this.memberRepository = memberRepository;
    }

    public TodosResponse findTodosContainsContent(String content, Pageable pageable, Boolean isDone) {
        if (isDone == null) {
            Page<Todo> resultTodos = todoRepository.findTodosContainsContent(content, pageable);
            return TodosResponse.map(resultTodos);
        }

        Page<Todo> resultTodos = todoRepository.findTodosContainsContentAndIsDone(content, pageable, isDone);

        return TodosResponse.map(resultTodos);
    }

    public TodosResponse findTodosContainsTitle(String title, Pageable pageable) {
        Page<Todo> resultTodos = todoRepository.findTodosContainsTitle(title, pageable);

        return TodosResponse.map(resultTodos);
    }

    @Transactional
    public TodoCreateResponse createTodo(TodoCreateRequest request, Long memberId) {
        // 실제 DB에서 값을 가져오는 방법이 아닌 원본 객체의 프록시 객체를 가지고 있다.
        // 해당 엔티티의 식별자를 사용하여 프록시 객체를 생성한다.
        // 프록시 객체는 식별자 값을 가지고 있기 때문에 필요한 시점에 식별자를 이용하여 DB에서 데이터를 가져온다.
        // 이를 통해 불필요한 데이터베이스 조회를 피하고, 필요한 데이터만 가져오는 지연로딩(Lazy Loading) 기능을 제공
        // FetchType.LAZY도 비슷한 방식이다.
        Member member = Optional.ofNullable(memberRepository.getReferenceById(memberId))
                .orElseThrow(MemberNotFoundException::new);

        Todo todo = Todo.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .build();

        Todo savedTodo = todoRepository.save(todo);

        return TodoCreateResponse.map(savedTodo);
    }

    @Transactional
    public void updateTodo(TodoUpdateRequest request, Long memberId, Long todoId) {
        Todo todo = getTodoById(todoId);

        todo.isAuthor(memberId);

        todo.update(request);
    }

    @Transactional
    public void deleteTodo(Long memberId, Long todoId) {
        Todo todo = getTodoById(todoId);

        todo.isAuthor(memberId);

        todoRepository.deleteById(todo.getId());
    }

    @Transactional
    public void updateIsDoneStatus(Long memberId, Long todoId) {
        Member member = getMemberById(memberId);

        Todo todo = getTodoById(todoId);

        todo.isAuthor(memberId);

        todo.updateIsDone();
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Todo getTodoById(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(TodoNotFoundException::new);
    }
}
