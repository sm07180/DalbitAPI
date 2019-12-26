package com.demo.vo.helper;

import com.demo.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.Locale;

/**
 * 예외처리를 위한 VO
 */
@Getter
@Setter
public class ExceptionVo {

    ExceptionVo(){}

    public ExceptionVo(String code, String messageKey, HashMap data){
        setCode(code);
        setMessageKey(messageKey);
        setData(data);
        setTimestamp(StringUtil.getTimeStamp());
    }

    private String code;
    private String message;
    private String messageKey;
    private HashMap data;
    private String timestamp;
}
