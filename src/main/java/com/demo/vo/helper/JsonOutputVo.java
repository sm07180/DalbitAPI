package com.demo.vo.helper;

import com.demo.code.Status;
import com.demo.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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

    private String code;
    private String message;
    private String messageKey;
    private Object data;
    private Status status;
    private String timestamp;

    public void setStatus(Status status){
        this.code = status.getName();
        this.messageKey = status.getMessageKey();
    }

}
