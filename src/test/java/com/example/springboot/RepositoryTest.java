package com.example.springboot;

import com.example.springboot.common.config.QueryDslConfig;
import com.example.springboot.like.domain.entity.Like;
import com.example.springboot.like.repository.LikeRepository;
import com.example.springboot.member.domain.entity.Member;
import com.example.springboot.member.repository.MemberRepository;
import com.example.springboot.todo.domain.entity.Todo;
import com.example.springboot.todo.repository.TodoRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
//@Sql(value = "classpath:/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RepositoryTest {

    @Autowired
    protected TodoRepository todoRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected LikeRepository likeRepository;

    protected Member member1, member2, member3;

    protected Todo todo1, todo2, todo3, todo4, todo5, todo6;

    protected Like like1, like2, like3, like4, like5, like6;

    @BeforeEach
    protected void initDB() {
        Member member1 = memberRepository.save(Member.builder()
                .id(1L)
                .password("password1")
                .email("aaa@aaa.com")
                .name("name1")
                .age(1)
                .build()
        );

        Member member2 = memberRepository.save(Member.builder()
                .id(2L)
                .password("password2")
                .email("bbb@bbb.com")
                .name("name2")
                .age(2)
                .build()
        );

        Member member3 = memberRepository.save(Member.builder()
                .id(3L)
                .password("password3")
                .email("ccc@ccc.com")
                .name("name3")
                .age(3)
                .build()
        );

        Todo todo1 = todoRepository.save(Todo.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .member(member1)
                .build()
        );

        Todo todo2 = todoRepository.save(Todo.builder()
                .id(2L)
                .title("title2")
                .content("content2")
                .member(member1)
                .build()
        );

        Todo todo3 = todoRepository.save(Todo.builder()
                .id(3L)
                .title("title3")
                .content("content3")
                .member(member2)
                .build()
        );

        Todo todo4 = todoRepository.save(Todo.builder()
                .id(4L)
                .title("title4")
                .content("content4")
                .member(member2)
                .build()
        );

        Todo todo5 = todoRepository.save(Todo.builder()
                .id(5L)
                .title("title5")
                .content("content5")
                .member(member2)
                .build()
        );

        Todo todo6 = todoRepository.save(Todo.builder()
                .id(6L)
                .title("title6")
                .content("content6")
                .member(member2)
                .build()
        );
    }
}
