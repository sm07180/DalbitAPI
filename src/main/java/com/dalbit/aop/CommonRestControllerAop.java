package com.dalbit.aop;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;

/**
 * 공통 rest controller AOP 정의
 */
@Slf4j
@Aspect
@Component
public class CommonRestControllerAop {

    @Autowired
    HttpServletRequest request;

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

        log.info("[{}] [memNo : {}] - 실행시간 : {}", proceedName, memNo, stopWatch.getTotalTimeMillis() + " (ms)");
        log.debug("[{}] 헤더정보 : {}, ", proceedName, gsonUtil.toJson(new DeviceVo(request)));
        log.info("[memNo : {}] 실행결과 {}", memNo, gsonUtil.toJson(result));

        return result;
    }
}
