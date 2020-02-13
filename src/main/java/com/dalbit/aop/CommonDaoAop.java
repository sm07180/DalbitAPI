package com.dalbit.aop;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 공통 DAO AOP 정의
 */
@Slf4j
@Aspect
@Component
public class CommonDaoAop {

    /**
     * 공통 DAO 로깅처리
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.dalbit.*.dao.*.*(..))")
    public Object daoLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String proceedName = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();

        log.debug("[dao] - start : " + proceedName);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = proceedingJoinPoint.proceed();

        stopWatch.stop();

        log.info("[" + proceedName + "] - 실행시간 : " + stopWatch.getTotalTimeMillis() + " (ms)");
        log.info("[" + proceedName + "] - ### DB 리턴 결과 ### : " + new Gson().toJson(proceedingJoinPoint.getArgs()));

        return result;
    }
}
