package com.demo.util;

import com.demo.code.ErrorStatus;
import com.demo.exception.GlobalException;
import com.demo.vo.helper.ExceptionVo;
import com.demo.vo.helper.JsonOutputVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class MessageUtil{

    @Autowired
    private MessageSource messageSource;

    public String get(String code){
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    /**
     * 에러 vo 메시지 세팅
     * @param errorStatus
     * @param map
     * @return
     */
    public ExceptionVo setExceptionInfo(ErrorStatus errorStatus, HashMap map){
        ExceptionVo exceptionVo = GlobalException.throwException(errorStatus, map);
        exceptionVo.setMessage(messageSource.getMessage(exceptionVo.getMessageKey(), null, LocaleContextHolder.getLocale()));
        return exceptionVo;
    }

    /**
     * JsonOutPut 메시지 세팅
     * @param jsonOutputVo
     * @return
     */
    public JsonOutputVo setJsonOutputVo(JsonOutputVo jsonOutputVo){
        jsonOutputVo.setMessage(messageSource.getMessage(jsonOutputVo.getMessageKey(), null, LocaleContextHolder.getLocale()));
        return jsonOutputVo;
    }



}
