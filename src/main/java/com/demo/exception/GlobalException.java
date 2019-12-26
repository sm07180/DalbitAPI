package com.demo.exception;

import com.demo.code.ErrorStatus;
import com.demo.vo.helper.ExceptionVo;
import lombok.ToString;

import java.util.HashMap;

@ToString
public class GlobalException extends Exception {

    private static final long serialVersionUID = 1L;

    public static ExceptionVo throwException(ErrorStatus errorStatus){
        return throwException(errorStatus, null);
    }

    public static ExceptionVo throwException(ErrorStatus errorStatus, HashMap data){
        return new ExceptionVo(errorStatus.getCode(), errorStatus.getMessageKey(), data);
    }

    public static ExceptionVo throwException(String message){
        return throwException(message, null);
    }

    public static ExceptionVo throwException(String message, HashMap data){
        return new ExceptionVo(null, message, data);
    }
}
