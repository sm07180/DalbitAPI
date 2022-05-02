package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum ChatVideoStatus implements Status {
    //영상대화 차감기준
    차감기준조회_성공("0", "chat.video.condition.select.success", "영상대화 차감기준 조회 성공 시"),
    차감기준조회_회원아님("-1", "chat.video.condition.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    차감기준조회_실패("C006", "chat.video.condition.select.fail", "영상대화 차감기준 조회 실패 시"),
    //영상대화
    영상대화_취소("3", "chat.video.cancel.success", "영상대화 취소 시"),
    영상대화_거절("4", "chat.video.refuse.success", "영상대화 거절 시"),
    영상대화_일시정지("5", "chat.video.pause.success", "영상대화 일시정지 시"),
    영상대화_무응답("8", "chat.video.no.response.success", "영상대화 무응답 시"),
    영상대화_차단("9", "chat.video.cut.success", "영상대화 차단 시"),
    //영상대화 신청
    영상대화_신청("0", "chat.video.apply.success", "영상대화 신청 시"),
    영상대화_신청_회원아님("-1", "chat.video.apply.member.number.error", "회원아닐 시"),
    영상대화_신청_상대정지("-2", "chat.video.apply.target.member.stop", "상대방이 정지회원 시"),
    영상대화_신청_차단회원("-3", "chat.video.apply.black", "본인이 차단 시"),
    영상대화_신청_상대가차단("-4", "chat.video.apply.target.black", "상대방이 차단했을 시"),
    영상대화_신청_메시지비활성("-5", "chat.video.apply.mailbox.off", "메시지 비활성 시"),
    영상대화_신청_메시지상대비활성("-6", "chat.video.apply.target.mailbox.off", "상대가 메시지 비활성 시"),
    영상대화_신청_수신거부("-7", "chat.video.apply.target.response.refuse", "수신거부 상태"),
    영상대화_신청_시간수신거부("-8", "chat.video.apply.target.response.time.refuse", "시간 수신거부 상태"),
    영상대화_신청_상대가수신거부("-9", "chat.video.apply.target.refuse", "상대가 수신거부 상태"),
    영상대화_신청_상대_시간수신거부("-10", "chat.video.apply.target.time.refuse", "상대가 시간 수신거부 상태"),
    영상대화_신청_방송참여중("-11", "chat.video.apply.room.join", "방송 참여중 상태"),
    영상대화_신청_상대_방송참여중("-12", "chat.video.apply.target.room.join", "상대가 방송 참여중 상태"),
    영상대화_신청_대화중("-13", "chat.video.apply.already", "영상 대화중"),
    영상대화_신청_상대_대화중("-14", "chat.video.apply.target.already", "상대가 영상 대화중"),
    영상대화_신청_달부족("-15", "chat.video.apply.dal.limit", "달 부족 시"),
    영상대화_실패("C006", "chat.video.apply.fail", "영상대화 신청 실패 시"),
    //영상대화 수락
    영상대화_수락("0", "chat.video.ok.success", "영상대화 수락 시"),
    영상대화_수락_실패("C006", "chat.video.ok.fail", "영상대화 수락 실패 시"),
    //영상대화 연결
    영상대화_연결("0", "chat.video.connect.success", "영상대화 연결 시"),
    영상대화_연결_실패("C006", "chat.video.connect.fail", "영상대화 연결 실패 시"),
    //영상대화 종료
    영상대화_종료("0", "chat.video.end.success", "영상대화 종료 시"),
    영상대화_종료_실패("C006", "chat.video.end.fail", "영상대화 종료 실패 시");


    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    ChatVideoStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
