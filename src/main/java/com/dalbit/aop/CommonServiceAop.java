package com.dalbit.aop;

import com.dalbit.member.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;

/**
 * 공통 service AOP 정의
 */
@Slf4j
@Aspect
@Component
public class CommonServiceAop {

    @Autowired
    HttpServletRequest request;

    /**
     * 전체 서비스 로깅
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.dalbit.*.service.*.*(..))"
            + "&& !@annotation(com.dalbit.common.annotation.NoLogging)")
    public Object serviceLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String memNo = request == null ? null : new MemberVo().getMyMemNo(request);
        String proceedName = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();

        log.debug("[service] [memNo : {}] - start : {}", memNo, proceedName);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = proceedingJoinPoint.proceed();

        stopWatch.stop();

        log.info("[" + proceedName + "] [memNo : {}] - 실행시간 : {}", memNo, stopWatch.getTotalTimeMillis() + " (ms)");

        return result;
    }

    /**
     * 이메일 발송 전 후 처리
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.dalbit.common.service.EmailService.sendEmail(..))")
    public Object emailServiceLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String proceedName = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();

        log.debug("[service] - start : " + proceedName);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        //email service가 email database를 바라 보도록 설정한다.
        request.setAttribute("datasource", "email");

        Object result = proceedingJoinPoint.proceed();

        //service 실행 후 제거
        request.removeAttribute("datasource");

        stopWatch.stop();

        log.info("[" + proceedName + "] - 실행시간 : " + stopWatch.getTotalTimeMillis() + " (ms)");

        return result;
    }
}
