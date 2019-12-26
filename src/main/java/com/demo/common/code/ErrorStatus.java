package com.demo.common.code;

public enum ErrorStatus {

    권한없음("E403", "http.error.accessDenied", "권한이 없는 페이지 접근 시"),
    잘못된호출("E404", "http.error.notFound", "404 혹은 파라메터가 빠진경우"),

    ;

    final private String code;
    final private String messageKey;
    final private String desc;

    public String getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }


    ErrorStatus(String code, String message, String desc){
        this.code = code;
        this.messageKey = message;
        this.desc = desc;
    }
}
