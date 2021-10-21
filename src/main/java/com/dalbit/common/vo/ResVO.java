package com.dalbit.common.vo;

import lombok.Data;

@Data
public class ResVO {
    private String code     = "99999"; // Fail
    private String message  = ""     ; //
    private Object data              ;

    public void setResVO (String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
