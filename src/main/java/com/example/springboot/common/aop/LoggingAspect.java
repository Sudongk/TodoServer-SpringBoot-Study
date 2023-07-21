package com.example.springboot.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.example.springboot.member.controller.MemberController.*(..))")
    public void loggingBefore() {
        log.info("{} : {}", this.getClass().getName(), LocalDateTime.now());
    }

    @After("execution(* com.example.springboot.member.controller.MemberController.*(..))")
    public void loggingAfter() {
        log.info("{} : {}", this.getClass().getName(), LocalDateTime.now());
    }

    @Around("execution(* com.example.springboot.member.*.*.*(..))")
    public Object loggingAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = new Date().getTime();
        log.info("start : {}, {}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        Object proceed = joinPoint.proceed();
        log.info("after : {}, {}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        long endTime = new Date().getTime();
        log.info("ms : {}", endTime - startTime);

        return proceed;
    }

    @Around("@annotation(org.springframework.stereotype.Service)")
    public Object servieLoggingAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = new Date().getTime();
        log.info("start : {}, {}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        Object proceed = joinPoint.proceed();
        log.info("after : {}, {}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        long endTime = new Date().getTime();
        log.info("ms : {}", endTime - startTime);

        return proceed;
    }
}
