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

    public void setSuccessResVO (Object data) {
        this.code = ResMessage.C00000.getCode();
        this.message = ResMessage.C00000.getCodeNM();
        this.data = data;
    }

    public void setFailResVO () {
        this.code = ResMessage.C99999.getCode();
        this.message = ResMessage.C99999.getCodeNM();
        this.data = null;
    }
}
