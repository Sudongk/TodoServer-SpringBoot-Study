package com.example.springboot.member.repository;

import com.example.springboot.member.dto.request.MemberCondition;
import com.example.springboot.member.dto.response.MemberResponse;
import com.example.springboot.member.dto.response.QMemberResponse;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.springboot.member.domain.entity.QMember.*;
import static com.example.springboot.todo.domain.entity.QTodo.*;

public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomMemberRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<MemberResponse> findAllMemberByCondition(MemberCondition memberCondition, Long memberId, Pageable pageable) {
        List<MemberResponse> results = jpaQueryFactory
                .select(new QMemberResponse(member))
                .from(member)
                .leftJoin(member.todos, todo)
                .fetchJoin()
                .where(
                        excludeMe(memberId),
                        nameContains(memberCondition.getName()),
                        isAgeGoe(memberCondition.getAgeLoe()),
                        isAgeLoe(memberCondition.getAgeLoe())
                )
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = Optional.ofNullable(
                jpaQueryFactory
                        .select(member.id.count())
                        .from(member)
                        .where(
                                excludeMe(memberId),
                                nameContains(memberCondition.getName()),
                                isAgeGoe(memberCondition.getAgeLoe()),
                                isAgeLoe(memberCondition.getAgeLoe())
                        )
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression excludeMe(Long memberId) {
        return memberId == null ? null : member.id.ne(memberId);
    }

    private BooleanExpression nameContains(String name) {
        return name == null ? null : member.name.contains(name);
    }

    private BooleanExpression isAgeGoe(Integer ageGoe) {
        return ageGoe == null ? null : member.age.goe(ageGoe);
    }

    private BooleanExpression isAgeLoe(Integer ageLoe) {
        return ageLoe == null ? null : member.age.loe(ageLoe);
    }
}
