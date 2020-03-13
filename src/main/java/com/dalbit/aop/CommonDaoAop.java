package com.dalbit.aop;

import com.dalbit.member.vo.MemberVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;

/**
 * 공통 DAO AOP 정의
 */
@Slf4j
@Aspect
@Component
public class CommonDaoAop {

    @Autowired
    HttpServletRequest request;

    /**
     * 공통 DAO 로깅처리
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.dalbit.*.dao.*.*(..))")
    public Object daoLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String memNo = request == null ? null : new MemberVo().getMyMemNo(request);
        String proceedName = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();

        log.debug("[dao] [memNo : {}] - start : {}", memNo, proceedName);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = proceedingJoinPoint.proceed();

        stopWatch.stop();

        log.info("[" + proceedName + "] [memNo : {}] - 실행시간 : {}", memNo, stopWatch.getTotalTimeMillis() + " (ms)");
        log.info("[" + proceedName + "] [memNo : {}] - ### DB 리턴 결과 ### : {}", memNo, new Gson().toJson(proceedingJoinPoint.getArgs()));

        return result;
    }
}
