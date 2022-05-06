package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum AdminStatus implements Status {
    //////////////////////////
    //모바일 관리자
    /////////////////////////
    방송강제종료_성공("0", "force.exit.success", "강제종료 성공"),
    방송강제종료_회원아님("-1", "force.exit.no.member", "강제종료 요청 시 잘못된 회원번호"),
    방송강제종료_존재하지않는방("-2", "force.exit.no.room", "강제종료 시 존재하지 않는 방"),
    방송강제종료_이미종료된방("-3", "force.exit.already.end", "이미 종료된 방을 강제종료 한 경우"),
    방송강제종료_방참가자아님("-4", "force.exit.no.listener", "방 참가자가 아닌경우"),
    관리자메뉴조회_권한없음("-1", "no.auth", "관리자 권한이 없는 경우"),
    // 회원 방송방 숨김 처리
    회원방송방_숨김_해제_성공("0", "admin.broadcast.hide.success", "회원방송방 숨김 또는 해제 성공 시"),
    회원방송방_숨김_해제_실패("-1", "admin.broadcast.hide.fail", "회원방송방 숨김 또는 해제 실패 시");


    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    AdminStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
