package com.demo.common.code;

import lombok.Getter;

@Getter
public enum Status {

    //로그인
    로그인("0001", "login.success", "로그인 성공 시"),
    로그인실패("0002", "login.fail", "로그인 실패 시"),

    //CRUD
    조회("1001", "read.success", "조회"),
    수정("1002", "update.success", "수정"),
    생성("1003", "create.success", "생성"),
    삭제("1004", "delete.success", "삭제"),

    //방송
    방송참여("2001", "broadcast.in", "방송 참여 시"),
    방송나가기("2002", "broadcast.out", "방송 나가기 시"),
    방송생성("2003", "broadcast.start", "방송 생성 시"),
    방송종료("2004", "broadcast.end", "방송 종료 시"),

    //유저
    매니저지정("3001", "broduser.manager.add", "매니저 지정 시"),
    게스트초대("3003", "broduser.guest.invite", "게스트 초대 시"),
    게스트초대수락("3005", "broduser.guest.join", "게스트 초대 수락 시"),
    게스트신청("3006", "broduser.guest.apply", "게스트 신청 시")
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
