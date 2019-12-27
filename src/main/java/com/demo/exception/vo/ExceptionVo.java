package com.demo.exception.vo;

import com.demo.common.code.ErrorStatus;
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


    public ExceptionVo(ErrorStatus errorStatus, HashMap data){
        setResult(errorStatus.getResult());
        setErrorCode(errorStatus.getErrorCode());
        setMessageKey(errorStatus.getMessageKey());
        setData(data);
        setTimestamp(StringUtil.getTimeStamp());
    }

    public ExceptionVo(String errorCode, String messageKey, HashMap data){
        setErrorCode(errorCode);
        setMessageKey(messageKey);
        setData(data);
        setTimestamp(StringUtil.getTimeStamp());
    }

    private String result;
    private String errorCode;
    private String message;
    private String messageKey;
    private HashMap data;
    private String timestamp;
}
