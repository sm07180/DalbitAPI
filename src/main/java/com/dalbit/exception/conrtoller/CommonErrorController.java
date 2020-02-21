package com.dalbit.exception.conrtoller;

import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
@RestController
public class CommonErrorController{

    @Autowired
    GsonUtil gsonUtil;

    @ExceptionHandler(GlobalException.class)
    public String exceptionHandle(GlobalException globalException, HttpServletResponse response, HttpServletRequest request){
        DalbitUtil.setHeader(request, response);

        if(globalException.getErrorStatus() != null){
            return gsonUtil.toJson(new JsonOutputVo(globalException.getErrorStatus(), globalException.getData(), globalException.getValidationMessageDetail()));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(globalException.getStatus(), globalException.getData(), globalException.getValidationMessageDetail()));
        }

    }
}
