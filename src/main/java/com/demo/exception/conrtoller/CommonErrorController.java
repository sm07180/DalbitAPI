package com.demo.exception.conrtoller;

import com.demo.exception.GlobalException;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

@ApiIgnore
@Slf4j
@ControllerAdvice
@RestController
public class CommonErrorController{

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    GsonUtil gsonUtil;

    @ExceptionHandler(GlobalException.class)
    public String exceptionHandle(GlobalException globalException){

        /*Object errorStatus = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorRequestUrl = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);*/

        HashMap map = new HashMap();
        /*map.put("httpErrorCode", errorStatus);
        map.put("requestUrl", errorRequestUrl);
        map.put("message", errorMessage);*/

        log.error(new Gson().toJson(map));

        //return new Gson().toJson(messageUtil.setExceptionInfo(globalException.getErrorStatus(), null));
        return gsonUtil.toJson(globalException.getErrorStatus(), null);
        //return new ModelAndView("error/"+errorStatus);
    }
}
