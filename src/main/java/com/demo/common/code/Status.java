package com.demo.common.code;

import lombok.Getter;

@Getter
public enum Status {

    //공통
    파라미터오류("0000", "param.error", "파라미터 오류 시"),

    //로그인
    로그인("0001", "login.success", "로그인 성공 시"),
    로그인실패("0002", "login.fail", "로그인 실패 시"),
    회원가입필요("0003", "login.join.need", "회원가입 필요 시"),
    패스워드틀림("0004", "login.fail", "로그인 실패 시"),

    //회원가입
    회원가입("4001", "member.join.success", "회원가입 성공 시"),
    중복가입("4002", "member.join.already", "이미 회원가입된 상태 시"),
    닉네임중복("4003", "member.join.nick.duplicate", "닉네임 중복 시"),
    닉네임사용가능("4004", "member.join.nick.possible", "닉네임 사용가능 시"),
    회원가입오류("4005", "member.join.error", "회원가입 오류 시"),

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

    //방송 행위
    좋아요("2101", "broadcast.like", "좋아요 선택 시"),
    좋아요취소("2102", "broadcast.unlike", "좋아요 취소 시"),
    부스트("2103", "broadcast.boost", "부스트 선택 시"),

    //유저
    매니저지정("3001", "broduser.manager.add", "매니저 지정 시"),
    게스트초대("3003", "broduser.guest.invite", "게스트 초대 시"),
    게스트초대수락("3005", "broduser.guest.join", "게스트 초대 수락 시"),
    게스트신청("3006", "broduser.guest.apply", "게스트 신청 시"),

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
