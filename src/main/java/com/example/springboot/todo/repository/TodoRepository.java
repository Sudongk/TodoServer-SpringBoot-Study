package com.example.springboot.todo.repository;

import com.example.springboot.todo.domain.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TodoRepository extends JpaRepository<Todo, Long>, CustomTodoRepository {

    @Query("select t from Todo t left join fetch t.member where t.content like %:content%")
    Page<Todo> findTodosContainsContent(@Param("content") String content, Pageable pageable);

    @Query("select t from Todo t left join fetch t.member where t.isDone = :isDone and t.content like %:content%")
    Page<Todo> findTodosContainsContentAndIsDone(@Param("content") String content, Pageable pageable, @Param("isDone") Boolean isDone);

    @Query("select t from Todo t left join fetch t.member where t.title like %:title%")
    Page<Todo> findTodosContainsTitle(@Param("title") String title, Pageable pageable);
}
