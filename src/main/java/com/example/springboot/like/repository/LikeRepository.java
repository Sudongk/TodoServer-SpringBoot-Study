package com.example.springboot.like.repository;

import com.example.springboot.like.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("select l from Like l where l.member.id = :memberId and l.todo.id = :todoId")
    Optional<Like> findByMemberIdAndTodoId(@Param("memberId") Long memberId, @Param("todoId") Long todoId);
}

