package com.demo.common.code;

public enum Status {

    //로그인
    로그인("0001", "login.success", "로그인 성공 시"),
    로그인실패("0002", "login.fail", "로그인 실패 시"),

    //CRUD
    조회("1001", "read.success", "조회");

    ;

    final private String code;
    final private String messageKey;
    final private String desc;

    public String getName() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getDesc() {
        return desc;
    }

    Status(String code, String messageKey, String desc){
        this.code = code;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
