package com.example.springboot.todo.repository;

import com.example.springboot.todo.domain.entity.Todo;
import com.example.springboot.todo.dto.request.TodoCondition;
import com.example.springboot.todo.dto.response.QTodoResponse;
import com.example.springboot.todo.dto.response.TodoResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.springboot.member.domain.entity.QMember.*;
import static com.example.springboot.todo.domain.entity.QTodo.*;

public class CustomTodoRepositoryImpl implements CustomTodoRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomTodoRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Todo> findAllByCondition(TodoCondition todoCondition, Pageable pageable) {
        List<Todo> results = jpaQueryFactory
                .select(todo)
                .from(todo)
                .leftJoin(todo.member, member)
                .fetchJoin()
                .where(
                        contentContains(todoCondition.getContent()),
                        titleEq(todoCondition.getTitle()),
                        isDoneEq(todoCondition.getIsDone()),
                        isLikeGoe(todoCondition.getLikeGoe()),
                        isLikeLoe(todoCondition.getLikeLoe())
                )
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .fetch();

        // offset과 limit이 걸리지 않은 조건에 만족하는 모든 데이터
        long totalCount = Optional.ofNullable(
                jpaQueryFactory
                    .select(todo.count())
                    .from(todo)
                    .leftJoin(todo.member, member)
                    .where(
                            contentContains(todoCondition.getContent()),
                            titleEq(todoCondition.getTitle()),
                            isDoneEq(todoCondition.getIsDone()),
                            isLikeGoe(todoCondition.getLikeGoe()),
                            isLikeLoe(todoCondition.getLikeLoe())
                    )
                    .fetchOne())
                    .orElse(0L);

        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<TodoResponse> findAllByConditionV2(TodoCondition todoCondition, Pageable pageable) {
        List<TodoResponse> results = jpaQueryFactory
                .select(new QTodoResponse(todo))
                .from(todo)
                .leftJoin(todo.member, member)
                .fetchJoin()
                .where(
                        contentContains(todoCondition.getContent()),
                        titleEq(todoCondition.getTitle()),
                        isDoneEq(todoCondition.getIsDone()),
                        isLikeGoe(todoCondition.getLikeGoe()),
                        isLikeLoe(todoCondition.getLikeLoe())
                )
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .fetch();

        // offset과 limit이 걸리지 않은 조건에 만족하는 모든 데이터
        long totalCount = Optional.ofNullable(
                        jpaQueryFactory
                                .select(todo.id.count())
                                .from(todo)
                                .leftJoin(todo.member, member)
                                .where(
                                        contentContains(todoCondition.getContent()),
                                        titleEq(todoCondition.getTitle()),
                                        isDoneEq(todoCondition.getIsDone()),
                                        isLikeGoe(todoCondition.getLikeGoe()),
                                        isLikeLoe(todoCondition.getLikeLoe())
                                )
                                .fetchOne())
                .orElse(0L);

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression contentContains(String content) {
        return content == null ? null : todo.content.contains(content);
    }

    private BooleanExpression titleEq(String title) {
        return title == null ? null : todo.content.contains(title);
    }

    private BooleanExpression isDoneEq(Boolean isDone) {
        return isDone == null ? null : todo.isDone.eq(isDone);
    }

    private BooleanExpression isLikeGoe(Integer likeGoe) {
        return likeGoe == null ? null : todo.likeCount.goe(likeGoe);
    }

    private BooleanExpression isLikeLoe(Integer likeLoe) {
        return likeLoe == null ? null : todo.likeCount.goe(likeLoe);
    }
}
