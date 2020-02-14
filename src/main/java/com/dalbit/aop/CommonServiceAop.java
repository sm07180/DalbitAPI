package com.dalbit.aop;

import com.dalbit.member.vo.MemberVo;
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
    @Around("execution(* com.dalbit.*.service.*.*(..))")
    public Object serviceLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String memNo = MemberVo.getMyMemNo();
        String proceedName = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();

        log.debug("[service] [memNo : {}] - start : {}", memNo, proceedName);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = proceedingJoinPoint.proceed();

        stopWatch.stop();

        log.info("[" + proceedName + "] [memNo : {}] - 실행시간 : {}", memNo, stopWatch.getTotalTimeMillis() + " (ms)");

        return result;
    }
}
