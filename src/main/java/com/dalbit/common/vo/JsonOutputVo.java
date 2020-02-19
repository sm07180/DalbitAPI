package com.dalbit.common.vo;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.code.Status;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * JSON output을 위한 VO
 */
@Getter
@Setter
public class JsonOutputVo {

    public JsonOutputVo(){}

    public JsonOutputVo(Status status){
        setStatus(status);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
    }

    public JsonOutputVo(Status status, Object data){
        setStatus(status);
        setData(data);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
    }

    public JsonOutputVo(Status status, Object data, ArrayList validationMessageDetail){
        setStatus(status);
        setData(data);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());

        setValidationMessageDetail(validationMessageDetail);
    }

    public JsonOutputVo(ErrorStatus errorStatus){
        setErrorStatus(errorStatus);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
    }

    public JsonOutputVo(ErrorStatus errorStatus, Object data){
        setErrorStatus(errorStatus);
        setData(data);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());
    }

    public JsonOutputVo(ErrorStatus errorStatus, Object data, ArrayList validationMessageDetail){
        setErrorStatus(errorStatus);
        setData(data);
        setTimestamp(DalbitUtil.setTimestampInJsonOutputVo());

        setValidationMessageDetail(validationMessageDetail);
    }

    private String result;

    private String code;
    private String messageKey;
    private String message;

    private Object data;

    private String timestamp;

    private ArrayList validationMessageDetail = new ArrayList<String>();

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
