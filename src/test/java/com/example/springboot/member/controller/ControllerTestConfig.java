package com.example.springboot.member.controller;

import com.example.springboot.like.service.LikeService;
import com.example.springboot.member.service.MemberService;
import com.example.springboot.todo.controller.TodoController;
import com.example.springboot.todo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest({
        MemberController.class,
        TodoController.class,
})
@ActiveProfiles("test")
public abstract class ControllerTestConfig {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected TodoService todoService;

    @MockBean
    protected LikeService likeService;

    // JpaMetamodelMappingContext는 Spring Data JPA와 함께 동작하기 위해 필요한 컴포넌트 중 하나
    @MockBean
    protected JpaMetamodelMappingContext jpaMetamodelMappingContext;

}
