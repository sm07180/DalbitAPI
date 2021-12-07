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
    C30003("30003", "이벤트가 종료되었습니다."), // 11월 이벤트 종료

    // 깐부 이벤트
    C30004("30004", "이미 깐부가 있는 회원입니다."), // 신청대상자 깐부있음
    C30005("30005", "깐부는 최대 2명에게 신청 가능합니다.\n신청 취소 후 다시 접근해 주세요."), // 신청건수초과
    C30006("30006", "해당 회원은 탈퇴/정지된 회원입니다."), // 신청대상자 탈퇴&정지회원
    C30007("30007", "이벤트 기간이 아닙니다."), // 이벤트기간 아님
    C30008("30008", "신청내역이 없습니다."), // 신청내역 없음
    C30009("30009", "상대방이 이미 신청을 수락해 깐부가 맺어졌습니다."), // 이미 수락된 데이터
    C30010("30010", "나에게 신청을 한 회원입니다."), // 상대가 나에게 이미 신청
    C30011("30011", "이미 동일 회원에게 신청을 했습니다."), // 동일회원에게 이미 신청
    C30012("30012", "로그인 후 신청해주세요."), // 회원 아님
    C30013("30013", "깐부가 없습니다."), // 깐부 없음
    C30014("30014", "이미 지급되었습니다."), // 이미 지급됨
    C30015("30015", "구슬 개수가 부족합니다."), // 구슬 개수 부족
    C30016("30016", "배팅할 구슬 개수가 부족합니다."), // 배팅 구슬 개수 부족
    C30017("30017", "하루 2회만 베팅 가능합니다."), // 하루 2회만 베팅 가능

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
