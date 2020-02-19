package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum ErrorStatus {

    권한없음("E403", "http.error.accessDenied", "권한이 없는 페이지 접근 시"),
    잘못된호출("E404", "http.error.notFound", "404 혹은 파라메터가 빠진경우"),
    잘못된파람("E405", "http.error.param", "파라메터가 잘못경우"),
    호출에러("E406", "http.error.rest", "외부 서버 오류일 경우"),

    쿠키생성오류("E001", "cookie.generate.error", "쿠키 생성 시 에러"),
    쿠키갱신오류("E002", "cookie.update.error", "쿠키 갱신 시 에러"),
    쿠키만료오류("E003", "cookie.expire.error", "쿠키 만료 시 에러"),

    토큰검증오류("E004", "authToken.invalid.error", "JWT 토큰 검증 시 발생한 에러"),
    토큰만료오류("E005", "authToken.expire.error", "JWT 토큰 만료 시 발생한 에러"),

    커스텀헤더정보이상("E006", "request.custom.header.error", "request header의 custom_header 정보가 이상할 때"),
    ;

    final private String ERROR_RESULT = "fail";

    final private String result;

    final private String errorCode;
    final private String messageKey;
    final private String desc;

    ErrorStatus(String errorCode, String messageKey, String desc){
        this.result = ERROR_RESULT;
        this.errorCode = errorCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
