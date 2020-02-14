package com.dalbit.aop;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 공통 rest controller AOP 정의
 */
@Slf4j
@Aspect
@Component
public class CommonRestControllerAop {

    @Autowired
    GsonUtil gsonUtil;

    /**
     * 공통 컨트롤러 로깅처리
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.dalbit.*.controller.*.*(..))")
    public Object restControllerLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String memNo = MemberVo.getMyMemNo();
        String proceedName = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();

        log.debug("[restController] [memNo : {}] - start : {}", memNo, proceedName);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = proceedingJoinPoint.proceed();

        stopWatch.stop();

        log.info("[" + proceedName + "] [memNo : {}] - 실행시간 : {}", memNo, stopWatch.getTotalTimeMillis() + " (ms)");
        log.info("실행결과 : " + gsonUtil.toJson(result));

        return result;
    }
}
