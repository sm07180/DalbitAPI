package com.demo.common.vo;

import com.demo.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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
