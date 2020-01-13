package com.demo.exception;

import com.demo.common.code.ErrorStatus;
import com.demo.common.vo.JsonOutputVo;
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

    public static JsonOutputVo throwException(ErrorStatus errorStatus){
        return throwException(errorStatus, null);
    }

    public static JsonOutputVo throwException(ErrorStatus errorStatus, HashMap data){
        return new JsonOutputVo(errorStatus, data);
    }
}
