package com.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 공통 service AOP 정의
 */
@Slf4j
@Aspect
@Component
public class CommonServiceAop {

    /**
     * 전체 서비스 로깅
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.demo.service.*.*(..))")
    public Object serviceLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String proceedName = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();

        log.debug("[service] - start : " + proceedName);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = proceedingJoinPoint.proceed();

        stopWatch.stop();

        log.info("[" + proceedName + "] - 실행시간 : " + stopWatch.getTotalTimeMillis() + " (ms)");

        return result;
    }
}
