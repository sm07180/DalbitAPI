package com.demo.util;

import com.demo.common.vo.JsonOutputVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageUtil{

    @Autowired
    private MessageSource messageSource;

    public String get(String code){
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
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
