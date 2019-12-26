package com.demo.common.code;

import lombok.Getter;

@Getter
public enum Status {

    //로그인
    로그인("0001", "login.success", "로그인 성공 시"),
    로그인실패("0002", "login.fail", "로그인 실패 시"),

    //CRUD
    조회("1001", "read.success", "조회");

    ;

    final private String SUCCESS_RESULT = "success";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    Status(String messageCode, String messageKey, String desc){
        this.result = SUCCESS_RESULT;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
