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
    로그인실패("-1", "login.fail", "로그인 실패 시 - 아이디/비밀번호가 틀릴 시"),
    회원가입필요("1", "login.join.need", "회원가입 필요 시"),

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

    //방송생성
    방송생성("0", "broadcast.room.start", "방송 생성 시"),
    방송생성_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송중인방존재("-2", "broadcast.room.existence", "방송중인 방이 있을 시"),
    방생성실패("-3", "broadcast.room.fail", "방송 생성 실패 시"),

    //방송참여
    방참가성공("0", "broadcast.room.join.success", "방송 참가 성공 시"),
    방송참여_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송참여_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송참여_종료된방송("-3", "broadcast.room.end", "종료된 방송일 시"),
    이미참가("-4", "broadcast.room.join.already", "이미 참가 되어있을 시"),
    입장제한("-5", "broadcast.room.join.no", "입장제한 시"),
    나이제한("-6", "broadcast.room.join.no.age", "나이제한 시"),
    방참가실패("-7", "broadcast.room.join.fail", "방송 참가 실패 시"),

    //방송나가기
    방송나가기("0", "broadcast.room.out", "방송 나가기 시"),
    방송나가기_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송나가기_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송나가기_종료된방송("-3", "broadcast.room.end", "종료된 방송일 시"),
    방참가자아님("-4", "broadcast.room.join.member.no", "방송 참가자 아닐 시"),
    방송나가기실패("-5", "broadcast.out.fail", "방송 나가기 실패 시"),

    //방송종료
    방송종료("2004", "broadcast.end", "방송 종료 시"),

    //방송 행위
    좋아요("2101", "broadcast.like", "좋아요 선택 시"),
    좋아요취소("2102", "broadcast.unlike", "좋아요 취소 시"),
    부스트("2103", "broadcast.boost", "부스트 선택 시"),

    //유저
    매니저지정("3001", "broadcast.user.manager.add", "매니저 지정 시"),
    게스트초대("3003", "broadcast.user.guest.invite", "게스트 초대 시"),
    게스트초대수락("3005", "broadcast.user.guest.join", "게스트 초대 수락 시"),
    게스트신청("3006", "broadcast.user.guest.apply", "게스트 신청 시"),

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
