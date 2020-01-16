package com.demo.exception.handler;

import com.demo.common.code.ErrorStatus;
import com.demo.common.vo.JsonOutputVo;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    GsonUtil gsonUtil;

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String exceptionAdvice(HttpServletRequest httpServletRequest, Exception exception, HandlerMethod handlerMethod){

        Object errorStatus = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorRequestUrl = httpServletRequest.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object errorMessage = httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        String result = gsonUtil.toJson(new JsonOutputVo(ErrorStatus.권한없음));
        log.error(result);

        return result;
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ModelAndView arrayIndexOutOfBoundsExceptionHandler(HttpServletRequest req, Exception exception) {
        String result = "Request: " + req.getRequestURL() + " raised " + exception;
        log.error(result);

        //return result;
        return new ModelAndView("error/500");
    }

    /*
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception exception) {
        String result = "Request: " + req.getRequestURL() + " raised " + exception;
        log.error(result);

        //return result;
        return new ModelAndView("error/500");
    }
    */


}
