package com.dalbit.exception;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
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
    private boolean isCustomMessage = false;

    private ArrayList validationMessageDetail;

    public GlobalException(ErrorStatus errorStatus, String methodName){

        //printErrorLog(status, data, validationMessageDetail, methodName, isCustomMessage);

        setErrorStatus(errorStatus);
        setMethodName(methodName);
    }

    public GlobalException(ErrorStatus errorStatus, Object data, String methodName){



        setErrorStatus(errorStatus);
        setData(data);
        setMethodName(methodName);
    }

    public GlobalException(ErrorStatus errorStatus, Object data, ArrayList validationMessageDetail, String methodName){
        log.error(super.getMessage());
        setErrorStatus(errorStatus);
        setData(data);
        setValidationMessageDetail(validationMessageDetail);
        setMethodName(methodName);
    }


    public GlobalException(Status status, String methodName){

        printErrorLog(status, null, null, methodName, false);

        setStatus(status);
        setMethodName(methodName);
    }

    public GlobalException(Status status, Object data, String methodName){

        printErrorLog(status, data, null, methodName, false);

        setStatus(status);
        setData(data);
        setMethodName(methodName);
    }

    public GlobalException(Status status, Object data, ArrayList validationMessageDetail, String methodName){

        printErrorLog(status, data, validationMessageDetail, methodName, false);

        setStatus(status);
        setData(data);
        setValidationMessageDetail(validationMessageDetail);
        setMethodName(methodName);
    }

    public GlobalException(Status status, Object data, ArrayList validationMessageDetail, String methodName, boolean isCustomMessage){

        printErrorLog(status, data, validationMessageDetail, methodName, isCustomMessage);

        setStatus(status);
        setData(data);
        setValidationMessageDetail(validationMessageDetail);
        setMethodName(methodName);
        setCustomMessage(isCustomMessage);
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

    public void printErrorLog(Status status, Object data, ArrayList validationMessageDetail, String methodName, boolean isCustomMessage){
        if(status.getMessageCode().equals(Status.벨리데이션체크.getMessageCode())){
            log.error("messageCode : {}", status.getMessageCode());
            log.error("messageKey : {}", status.getMessageKey());
            log.error("desc : {}", status.getDesc());
            log.error("methodName : {}", methodName);
            validationMessageDetail.stream().forEach(list ->{
               log.error("validation message : {}", list.toString());
            });

        }else if(status.getMessageCode().equals(Status.비즈니스로직오류.getMessageCode())){
            log.error("messageCode : {}", status.getMessageCode());
            log.error("messageKey : {}", status.getMessageKey());
            log.error("desc : {}", status.getDesc());
            log.error("methodName : {}", methodName);
            log.error("{}", super.getStackTrace());

        }else if(status.getMessageCode().equals(Status.부적절한문자열.getMessageCode())){
            log.error("messageCode : {}", status.getMessageCode());
            log.error("messageKey : {}", status.getMessageKey());
            log.error("desc : {}", status.getDesc());
            log.error("methodName : {}", methodName);
            log.error("{}", super.getStackTrace());

        }

    }

}
