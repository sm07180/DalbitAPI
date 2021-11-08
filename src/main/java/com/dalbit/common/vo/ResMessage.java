package com.dalbit.common.vo;

import lombok.Getter;

@Getter
public enum ResMessage {
    /*
     * 성공 : 00000
     * 실패 : 99999
     *
     * Validation Msssage : 10001 ~
     * Service Logic : 30001 ~
     * Etc Message : 80001 ~
     * System Error : 90001 ~
     * */
    C00000("00000","SUCCESS"),

    C10001("10001","회원번호(MemNo)가 없습니다."),

    // 11월 이벤트
    C30001("30001", "11월 이벤트 경품 번호 없음"),
    C30002("30002", "보유중인 경품 응모권이 부족합니다."), // 11월 이벤트 응모권수 부족

    // 아이템 지급
    C50001("50001", "회원 및 아이템 수 없음"),

    C99995("99995","Invalid cookie"),
    C99996("99996","File Does Not Exist"),
    C99997("99997","DB Server Error"),
    C99998("99998","API Server Error"),
    C99999("99999","FAIL");

    private String code;    //코드 : 숫자4자리
    private String codeNM;  //코드설명 : 영문 or 한글

    ResMessage(String code, String codeNM) {
        this.code = code;
        this.codeNM = codeNM;
    }
}
