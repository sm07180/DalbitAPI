package com.demo.common.code;

import lombok.Getter;

@Getter
public enum Status {

    //공통
    파라미터오류("0000", "param.error", "파라미터 오류 시"),

    //CRUD
    조회("1001", "read.success", "조회"),
    수정("1002", "update.success", "수정"),
    생성("1003", "create.success", "생성"),
    삭제("1004", "delete.success", "삭제"),

    //로그인
    로그인("0", "login.success", "로그인 성공 시"),
    로그인실패("0002", "login.fail", "로그인 실패 시"),
    회원가입필요("1", "login.join.need", "회원가입 필요 시"),
    패스워드틀림("-1", "login.password.fail", "패스워드 틀림 시"),

    //회원가입
    회원가입("4001", "member.join.success", "회원가입 성공 시"),
    중복가입("4002", "member.join.already", "이미 회원가입된 상태 시"),
    닉네임중복("4003", "member.join.nick.duplicate", "닉네임 중복 시"),
    닉네임사용가능("4004", "member.join.nick.possible", "닉네임 사용가능 시"),
    회원가입오류("4005", "member.join.error", "회원가입 오류 시"),

    //비밀번호변경
    비밀번호변경성공("임시", "member.change.password.success", "비밀번호 변경 성공 시"),
    비밀번호변경실패("임시", "member.change.password.fail", "비밀번호 변경 실패 시"),

    //프로필편집
    프로필편집성공("임시", "member.edit.profile.success", "프로필 편집 성공 시"),
    프로필편집실패_회원아님("임시", "member.edit.profile.fail.notUser", "프로필 편집 실패 - 회원이 아닌경우"),
    프로필편집실패_닉네임중복("임시", "member.edit.profile.fail.duplicateNickName", "프로필 편집 실패 - 닉네임이 중복된 경우"),

    //방송
    방송참여("2001", "broadcast.in", "방송 참여 시"),
    방송나가기("2002", "broadcast.out", "방송 나가기 시"),
    방송생성("2003", "broadcast.start", "방송 생성 시"),
    방송종료("2004", "broadcast.end", "방송 종료 시"),
    회원아님("2005", "broadcast.member.no", "회원이 아닐 시"),
    방송중인방존재("2006", "broadcast.room.existence", "방송중인 방이 있을 시"),
    방생성실패("2007", "broadcast.fail", "방송 생성 실패 시"),
    방참가성공("2008", "broadcast.room.join.success", "방송 참가 성공 시"),
    방참가실패("2009", "broadcast.room.join.fail", "방송 참가 실패 시"),
    해당방이없음("2010", "broadcast.room.no", "해당 방이 없을 시"),
    종료된방송("2011", "broadcast.room.end", "종료된 방송일 시"),
    이미참가("2012", "broadcast.room.join.already", "이미 참가 되어있을 시"),
    입장제한("2013", "broadcast.room.join.no", "입장제한 시"),
    나이제한("2014", "broadcast.room.join.no.age", "나이제한 시"),
    방참가자아님("2015", "broadcast.room.join.member.no", "방송 참가자 아닐 시"),
    방송나가기실패("2016", "broadcast.out.fail", "방송 나가기 실패 시"),


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
