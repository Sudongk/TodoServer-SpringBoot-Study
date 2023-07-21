package com.example.springboot.like.service;

import com.example.springboot.like.domain.entity.Like;
import com.example.springboot.like.repository.LikeRepository;
import com.example.springboot.member.domain.entity.Member;
import com.example.springboot.member.exception.MemberNotFoundException;
import com.example.springboot.member.repository.MemberRepository;
import com.example.springboot.todo.domain.entity.Todo;
import com.example.springboot.todo.exception.TodoNotFoundException;
import com.example.springboot.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;

    public LikeService(LikeRepository likeRepository, MemberRepository memberRepository, TodoRepository todoRepository) {
        this.likeRepository = likeRepository;
        this.memberRepository = memberRepository;
        this.todoRepository = todoRepository;
    }

    @Transactional
    public void like(Long memberId, Long todoId) {
        Member member = getMemberById(memberId);

        Todo todo = getTodoById(todoId);

        updateLikeStatus(member, todo);
    }

    private void updateLikeStatus(Member member, Todo todo) {
        likeRepository.findByMemberIdAndTodoId(member.getId(), todo.getId())
                .ifPresentOrElse(
                        like -> {
                            // 값이 있는 경우
                            likeRepository.deleteById(like.getId());
                            todo.decreaseLikeCount();
                        },
                        () -> {
                            // 값이 없는 경우
                            likeRepository.save(new Like(member, todo));
                            todo.increaseLikeCount();
                        }
                );
    }

    private Todo getTodoById(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(TodoNotFoundException::new);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
