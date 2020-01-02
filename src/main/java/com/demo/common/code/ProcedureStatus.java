package com.demo.common.code;

import lombok.Getter;

@Getter
public enum ProcedureStatus {

    //프로시저
    성공(0, "procedure.success", "프로시저 결과 정상일 시"),
    실패(-1, "procedure.fail", "프로시저 결과 정상일 시"),

    ;

    final private int resultCode;
    final private String messageKey;
    final private String desc;

    ProcedureStatus(int resultCode, String messageKey, String desc){
        this.resultCode = resultCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }

}
