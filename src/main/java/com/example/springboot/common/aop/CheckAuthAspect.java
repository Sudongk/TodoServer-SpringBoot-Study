package com.example.springboot.common.aop;

import com.example.springboot.common.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class CheckAuthAspect {

    private final JwtProvider jwtProvider;

    public CheckAuthAspect(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Before("@annotation(com.example.springboot.common.aop.CheckAuth)")
    public void checkToken() {
        log.info("check token!");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        String token = jwtProvider.extractJwt(requestAttributes);

        log.info("token : {}", token);
        jwtProvider.getClaims(token);
    }


}
