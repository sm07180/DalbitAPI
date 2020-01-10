package com.demo.common.vo;

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
    }

    public JsonOutputVo(Status status, Object data){
        setStatus(status);
        setData(data);
        setTimestamp(StringUtil.getTimeStamp());
    }

    private String result;

    private Status status;

    private String messageCode;
    private String messageKey;
    private String message;

    private Object data;

    private String timestamp;
    private String errorCode;

    public void setStatus(Status status){
        //this.messageCode = status.getMessageCode();
        this.messageKey = status.getMessageKey();
        this.result = status.getResult();
    }

}
