package com.example.springboot;

import com.example.springboot.common.jwt.JwtProvider;
import com.example.springboot.like.repository.LikeRepository;
import com.example.springboot.member.repository.MemberLoginRepository;
import com.example.springboot.member.repository.MemberRepository;
import com.example.springboot.todo.repository.TodoRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@MockitoSettings
public abstract class ServiceTestConfig {

    @Mock
    protected MemberRepository memberRepository;

    @Mock
    protected MemberLoginRepository memberLoginRepository;

    @Mock
    protected LikeRepository likeRepository;

    @Mock
    protected TodoRepository todoRepository;

    @Mock
    protected JwtProvider jwtProvider;

}
