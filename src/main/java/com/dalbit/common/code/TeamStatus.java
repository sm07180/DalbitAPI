package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum TeamStatus implements Status {
    //팀 관련
    팀_등록체크_가입상태("-4", "team.chk.fail", "팀가입 되어 있음"),
    팀_등록체크_생성시간아님("-3", "team.chk.fail", "재생성 시간 미경과"),
    팀_등록체크_중복생성("-2", "team.chk.fail", "이미생성됨"),
    팀_등록체크_레벨미달("-2", "team.chk.under", "레벨미달"),
    팀_등록체크_에러("0", "team.chk.error", "팀 등록체크 에러"),
    팀_등록체크_성공("1", "team.chk.success", "팀 등록체크 정상"),
    팀_등록_중복("-4", "team.insert.fail", "팀이름 중복"),
    팀_등록_필요시간미달("-3", "team.insert.under", "재생성 시간 미경과"),
    팀_등록_이미생성됨("-2", "team.insert.fail", "이미생성됨"),
    팀_등록_팀없음("-1", "team.insert.under", "팀없음"),
    팀_등록_에러("0", "team.insert.error", "팀 등록 에러"),
    팀_등록_성공("1", "team.insert.success", "팀 등록 정상"),
    팀_정보수정_중복("-4", "team.upd.fail", "팀이름 중복"),
    팀_정보수정_필요시간미달("-3", "team.upd.under", "재생성 시간 미경과"),
    팀_정보수정_수정횟수초과("-2", "team.upd.over", "수정횟수초과"),
    팀_정보수정_팀없음("-1", "team.upd.under", "팀없음"),
    팀_정보수정_에러("0", "team.upd.error", "팀 정보수정 에러"),
    팀_정보수정_성공("1", "team.upd.success", "팀 정보수정 정상"),
    팀_삭제_권한없음("-2", "team.del.fail", "팀삭제권한 없음"),
    팀_삭제_미가입("-1", "team.del.fail", "미가입 회원"),
    팀_삭제_에러("0", "team.del.error", "팀 삭제 에러"),
    팀_삭제_성공("1", "team.del.success", "팀 삭제 정상"),
    팀_가입초대_초과("-7", "team.req.ins.fail", "정원초과"),
    팀_가입초대_미허용("-6", "team.req.ins.fail", "가입신청 미허용"),
    팀_가입초대_이미초대("-5", "team.req.ins.fail", "이미초대됨"),
    팀_가입초대_이미신청("-4", "team.req.ins.fail", "이미신청함"),
    팀_가입초대_가입상태("-3", "team.req.ins.fail", "팀에 가입되어있음"),
    팀_가입초대_팀없음("-2", "team.req.ins.fail", "팀없음"),
    팀_가입초대_레벨부족("-1", "team.req.ins.under", "레벨부족"),
    팀_가입초대_에러("0", "team.req.ins.error", "팀 등록 에러"),
    팀_가입초대_성공("1", "team.req.ins.success", "팀 등록 정상"),
    팀_가입수락_정원초과("-2", "team.mem.ins.fail", "정원초과"),
    팀_가입수락_신청내역없음("-2", "team.mem.ins.fail", "신청내역없음"),
    팀_가입수락_가입되어있음("-1", "team.mem.ins.under", "팀에 가입되어있음"),
    팀_가입수락_팀없음("-1", "team.mem.ins.under", "팀없음"),
    팀_가입수락_에러("0", "team.mem.ins.error", "팀 삭제 에러"),
    팀_가입수락_성공("1", "team.mem.ins.success", "팀 삭제 정상"),
    팀_탈퇴_팀장아님("-2", "team.mem.del.fail", "팀장아님"),
    팀_탈퇴_미가입("-1", "team.mem.del.fail", "미가입 회원"),
    팀_탈퇴_에러("0", "team.mem.del.error", "팀 삭제 에러"),
    팀_탈퇴_성공("1", "team.mem.del.success", "팀 삭제 정상"),
    팀_팀장변경_미가입("-1", "team.mem.upd.fail", "미가입 회원"),
    팀_팀장변경_에러("0", "team.mem.upd.error", "팀 삭제 에러"),
    팀_팀장변경_성공("1", "team.mem.upd.success", "팀 삭제 정상"),
    팀_신청거절취소_신청내역없음("-2", "team.req.del.fail", "신청내역없음"),
    팀_신청거절취소_권한자가아님("-1", "team.req.del.fail", "팀장 or 관리자아님"),
    팀_신청거절취소_에러("0", "team.req.del.error", "팀 삭제 에러"),
    팀_신청거절취소_성공("1", "team.req.del.success", "팀 삭제 정상"),
    팀_출석체크_출석완료("-2", "team.att.ins.fail", "출석완료"),
    팀_출석체크_미가입("-1", "team.att.ins.fail", "팀 미가입"),
    팀_출석체크_에러("0", "team.att.ins.error", "팀 삭제 에러"),
    팀_출석체크_성공("1", "team.att.ins.success", "팀 삭제 정상"),
    팀_활동배지_갯수초과("-3", "team.badge.upd.fail", "대표설정 배지수 초과"),
    팀_활동배지_미달성("-2", "team.badge.upd.fail", "출석완료"),
    팀_활동배지_팀장아님("-1", "team.badge.upd.fail", "팀 미가입"),
    팀_활동배지_에러("0", "team.badge.upd.error", "팀 삭제 에러"),
    팀_활동배지_성공("1", "team.badge.upd.success", "팀 삭제 정상");
    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    TeamStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
