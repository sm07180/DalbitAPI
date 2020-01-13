package com.demo.common.vo;

import com.demo.common.code.ErrorStatus;
import com.demo.common.code.Status;
import com.demo.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * JSON output을 위한 VO
 */
@Getter
@Setter
public class JsonOutputVo {

    public JsonOutputVo(){}

    public JsonOutputVo(Status status){
        setStatus(status);
        setTimestamp(StringUtil.getTimeStamp());
    }

    public JsonOutputVo(Status status, Object data){
        setStatus(status);
        setData(data);
        setTimestamp(StringUtil.getTimeStamp());
    }

    public JsonOutputVo(ErrorStatus errorStatus){
        setErrorStatus(errorStatus);
        setTimestamp(StringUtil.getTimeStamp());
    }

    public JsonOutputVo(ErrorStatus errorStatus, Object data){
        setErrorStatus(errorStatus);
        setData(data);
        setTimestamp(StringUtil.getTimeStamp());
    }

    private String result;

    private String code;
    private String messageKey;
    private String message;

    private Object data;

    private String timestamp;

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
