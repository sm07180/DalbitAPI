package com.dalbit.exception;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
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
    private String methodName;

    private ArrayList validationMessageDetail;

    public GlobalException(ErrorStatus errorStatus, String methodName){
        setErrorStatus(errorStatus);
        setMethodName(methodName);
    }

    public GlobalException(ErrorStatus errorStatus, Object data, String methodName){
        setErrorStatus(errorStatus);
        setData(data);
        setMethodName(methodName);
    }

    public GlobalException(ErrorStatus errorStatus, Object data, ArrayList validationMessageDetail, String methodName){
        setErrorStatus(errorStatus);
        setData(data);
        setValidationMessageDetail(validationMessageDetail);
        setMethodName(methodName);
    }


    public GlobalException(Status status, String methodName){
        setStatus(status);
        setMethodName(methodName);
    }

    public GlobalException(Status status, Object data, String methodName){
        setStatus(status);
        setData(data);
        setMethodName(methodName);
    }

    public GlobalException(Status status, Object data, ArrayList validationMessageDetail, String methodName){
        setStatus(status);
        setData(data);
        setValidationMessageDetail(validationMessageDetail);
        setMethodName(methodName);
    }

    public static JsonOutputVo throwException(ErrorStatus errorStatus, String methodName){
        return throwException(errorStatus, null, methodName);
    }

    public static JsonOutputVo throwException(ErrorStatus errorStatus, HashMap data, String methodName){
        return new JsonOutputVo(errorStatus, data, methodName);
    }

    public static JsonOutputVo throwException(Status status, Object data, String methodName){
        return throwException(status, data, methodName);
    }

}
