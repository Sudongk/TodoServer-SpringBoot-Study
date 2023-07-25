package com.example.springboot.member.controller;

import com.example.springboot.common.aop.CheckAuth;
import com.example.springboot.common.aop.MemberId;
import com.example.springboot.member.dto.request.MemberCondition;
import com.example.springboot.member.dto.request.MemberLoginRequest;
import com.example.springboot.member.dto.request.MemberSignUpRequest;
import com.example.springboot.member.dto.response.MemberLoginResponse;
import com.example.springboot.member.dto.response.MemberSignUpResponse;
import com.example.springboot.member.dto.response.MembersResponse;
import com.example.springboot.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/members")
@Validated
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpResponse> join(@RequestBody @Valid MemberSignUpRequest memberSignUpRequest) {
        MemberSignUpResponse memberSignUpResponse = memberService.saveMember(memberSignUpRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberSignUpResponse);
    }

    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(description = "성공", responseCode = "200"),
            @ApiResponse(description = "실패", responseCode = "400")
    })
    @Operation(description = "로그인", summary = "login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody @Valid MemberLoginRequest memberLoginRequest) {
        MemberLoginResponse memberLoginResponse = memberService.loginMember(memberLoginRequest);

        return ResponseEntity
                .ok()
                .body(memberLoginResponse);
    }

    @GetMapping
    public ResponseEntity<MembersResponse> findAllMember(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {

        MembersResponse membersResponse = memberService.findAllMember(PageRequest.of(page, size));

        return ResponseEntity
                .ok()
                .body(membersResponse);
    }

    // 자기 자신 재외
    @CheckAuth
    @GetMapping("/v2")
    public ResponseEntity<MembersResponse> findAllMemberByCondition(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            @RequestBody MemberCondition memberCondition,
            @MemberId Long memberId) {

        MembersResponse membersResponse = memberService.findAllMemberByCondition(memberCondition, memberId, PageRequest.of(page, size));

        return ResponseEntity
                .ok()
                .body(membersResponse);
    }
}
