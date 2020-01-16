package com.dalbit.exception.conrtoller;

import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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

        if(globalException.getErrorStatus() != null){
            return gsonUtil.toJson(new JsonOutputVo(globalException.getErrorStatus(), globalException.getData()));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(globalException.getStatus(), globalException.getData()));
        }

    }
}
