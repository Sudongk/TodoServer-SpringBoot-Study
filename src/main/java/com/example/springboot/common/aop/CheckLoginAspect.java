package com.example.springboot.common.aop;

import com.example.springboot.common.exception.UnAuthorizedException;
import com.example.springboot.member.domain.entity.MemberLogin;
import com.example.springboot.member.repository.MemberLoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class CheckLoginAspect {

    private final MemberLoginRepository memberLoginRepository;

    public CheckLoginAspect(MemberLoginRepository memberLoginRepository) {
        this.memberLoginRepository = memberLoginRepository;
    }

    // 주의! memberId 변수 위치 항상 맨 처음
    @Before(value = "@annotation(com.example.springboot.common.aop.CheckLogin) && args(memberId, ..)")
    public void isLogin(Long memberId) {
        log.info("CheckLoginAspect memberId : {}", memberId);
        MemberLogin memberLogin = memberLoginRepository.findByMemberId(memberId)
                .orElseThrow(UnAuthorizedException::new);

        LocalDateTime lastLoginTime = memberLogin.getCreatedAt();

        if (!LocalDateTime.now().isBefore(lastLoginTime.plusMinutes(10))) {
            memberLoginRepository.deleteById(memberLogin.getId());
            throw new UnAuthorizedException();
        }
    }
}
