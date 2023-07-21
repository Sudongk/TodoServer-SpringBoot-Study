package com.example.springboot.like.domain.entity;

import com.example.springboot.common.BaseEntity;
import com.example.springboot.member.domain.entity.Member;
import com.example.springboot.todo.domain.entity.Todo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "likes")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    public Like(final Member member, final Todo todo) {
        this.member = member;
        this.todo = todo;
    }
}
