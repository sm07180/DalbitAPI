package com.dalbit.exception;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
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
    private Status status;
    private Object data;

    public GlobalException(ErrorStatus errorStatus){
        setErrorStatus(errorStatus);
    }
    public GlobalException(Status status){
        setStatus(status);
    }

    public GlobalException(Status status, Object data){
        setStatus(status);
        setData(data);
    }

    public static JsonOutputVo throwException(ErrorStatus errorStatus){
        return throwException(errorStatus, null);
    }

    public static JsonOutputVo throwException(ErrorStatus errorStatus, HashMap data){
        return new JsonOutputVo(errorStatus, data);
    }

    public static JsonOutputVo throwException(Status status, Object data){
        return throwException(status, data);
    }

}