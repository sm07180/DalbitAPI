package com.demo.exception;

import com.demo.common.code.ErrorStatus;
import com.demo.exception.vo.ExceptionVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@ToString
@Setter
@Getter
public class GlobalException extends Exception {

    private static final long serialVersionUID = 1L;

    private String message;
    private String errorCode;
    private String messageKey;
    private ErrorStatus errorStatus;

    public GlobalException(){}
    public GlobalException(ErrorStatus errorStatus){
        setErrorStatus(errorStatus);
    }

    public static ExceptionVo throwException(ErrorStatus errorStatus){
        return throwException(errorStatus, null);
    }

    public static ExceptionVo throwException(ErrorStatus errorStatus, HashMap data){
        return new ExceptionVo(errorStatus, data);
    }

    public static ExceptionVo throwException(String message){
        return throwException(message, null);
    }

    public static ExceptionVo throwException(String message, HashMap data){
        return new ExceptionVo(null, message, data);
    }
}
