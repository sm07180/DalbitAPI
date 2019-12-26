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
