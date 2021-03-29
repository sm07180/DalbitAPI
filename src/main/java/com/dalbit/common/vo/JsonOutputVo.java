package com.dalbit.common.vo;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.code.Status;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * JSON output을 위한 VO
 */
@Getter
@Setter
@Scope("prototype")
public class JsonOutputVo {

    public JsonOutputVo(){}

    public JsonOutputVo(Status status){
        setStatus(status);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
    }

    public JsonOutputVo(Status status, Object data){
        setStatus(status);
        setData(DalbitUtil.isEmpty(data) ? new HashMap() : data);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
    }

    public JsonOutputVo(Status status, Object data, ArrayList validationMessageDetail, String methodName){
        setStatus(status);
        setData(data);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
        setValidationMessageDetail(validationMessageDetail);
        setMethodName(methodName);
    }

    public JsonOutputVo(ErrorStatus errorStatus){
        setErrorStatus(errorStatus);
        setData(new HashMap());
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
    }

    public JsonOutputVo(ErrorStatus errorStatus, Object data, String methodName){
        setErrorStatus(errorStatus);
        setData(data);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
        setMethodName(methodName);
    }

    public JsonOutputVo(ErrorStatus errorStatus, Object data, ArrayList validationMessageDetail, String methodName){
        setErrorStatus(errorStatus);
        setData(data);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
        setValidationMessageDetail(validationMessageDetail);
        setMethodName(methodName);
    }

    private String result;

    private String code;
    private String messageKey;
    private String message;

    private Object data;
    private String timestamp;
    private ArrayList validationMessageDetail = new ArrayList<String>();
    private String methodName;


    public void setStatus(Status status){
        setCode(status.getMessageCode());
        setMessageKey(status.getMessageKey());
        setResult(status.getResult());
    }

    public void setErrorStatus(ErrorStatus errorStatus){
        setCode(errorStatus.getErrorCode());
        setMessageKey(errorStatus.getMessageKey());
        setResult(errorStatus.getResult());
    }

}
