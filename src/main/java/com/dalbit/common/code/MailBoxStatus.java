package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum MailBoxStatus implements Status {


    //메시지 대화방 리스트
    메시지대화방_조회_성공("C001", "mailbox.list.select.success", "대화방 리스트 조회 성공 시"),
    메시지대화방_조회_없음("0", "mailbox.list.select.no.success", "대화방 리스트 없을 시"),
    메시지팬대화방_조회_회원아님("-1", "mailbox.list.select.member.number.error", "요청회원번호 회원 아닐 시"),
    메시지팬대화방_조회_레벨0("-2", "mailbox.list.select.level.0", "본인 레벨 0"),
    메시지팬대화방_조회_실패("C006", "mailbox.list.select.fail", "대화방 리스트 조회 실패 시"),
    //메시지 대화방 추가대상
    메시지대화방추가대상_조회_성공("C001", "mailbox.add.target.list.select.success", "추가 대상 목록조회 성공 시"),
    메시지대화방추가대상_조회_없음("0", "mailbox.add.target.list.select.no.success", "추가 대상 목록조회 없을 시"),
    메시지대화방추가대상_조회_회원아님("-1", "mailbox.add.target.list.select.member.number.error", "요청회원번호 회원 아닐 시"),
    메시지대화방추가대상_조회_실패("C006", "mailbox.add.target.list.select.fail", "추가 대상 목록조회 실패 시"),
    //대화방 입장
    대화방입장_신규입장_성공("1", "mailbox.chatroom.enter.new.success", "대화방 신규 입장 성공 시"),
    대화방입장_성공("0", "mailbox.chatroom.enter.success", "대화방 입장 성공 시"),
    대화방입장_요청회원아님("-1", "mailbox.chatroom.enter.member.number.error", "요청회원번호 회원 아닐 시"),
    대화방입장_대상회원아님("-2", "mailbox.chatroom.enter.target.member.number.error", "대상회원번호 회원 아닐 시"),
    대화방입장_본인안됨("-3", "mailbox.chatroom.enter.me.impossible", "본인에게 입장 불가 시"),
    대화방입장_차단회원("-4", "mailbox.chatroom.enter.black", "차단회원일 경우"),
    대화방입장_레벨0("-5", "mailbox.chatroom.enter.level.0", "본인 레벨 0"),
    대화방입장_대상레벨0("-6", "mailbox.chatroom.enter.target.level.0", "상대방 레벨 0"),
    대화방입장_실패("C006", "mailbox.chatroom.enter.fail", "대화방 입장 실패 시"),
    //대화방 퇴장
    대화방퇴장_성공("0", "mailbox.chatroom.exit.success", "대화방 퇴장 성공 시"),
    대화방퇴장_회원아님("-1", "mailbox.chatroom.exit.member.number.error", "요청회원번호 회원 아닐 시"),
    대화방퇴장_번호없음("-2", "mailbox.chatroom.exit.chat.number.error", "대화방번호 없을 시"),
    대화방퇴장_이미나감("-3", "mailbox.chatroom.exit.already", "대화방 이미 나가있는 경우일 시"),
    대화방퇴장_실패("C006", "mailbox.chatroom.exit.fail", "대화방 퇴장 실패 시"),
    //대화전송
    대화전송_성공("0", "mailbox.chatroom.msg.send.success", "대화 전송 성공 시"),
    대화전송_회원아님("-1", "mailbox.chatroom.msg.send.member.number.error", "요청회원번호 회원 아닐 시"),
    대화전송_채팅번호없음("-2", "mailbox.chatroom.msg.send.chat.number.error", "대화방번호 없을 시"),
    대화전송_상대회원아님("-3", "mailbox.chatroom.msg.send.target.member.number.error", "상대회원번호 회원 아닐 시"),
    대화전송_아이템코드없음("-4", "mailbox.chatroom.msg.send.item.code.no", "아이템 코드가 없을 시"),
    대화전송_아이템타입없음("-5", "mailbox.chatroom.msg.send.item.type.no", "아이템 타입이 없을 시"),
    대화전송_달부족("-6", "mailbox.chatroom.msg.send.dal.limit", "달 부족 시"),
    대화전송_차단회원("-7", "mailbox.chatroom.msg.send.blacklist", "차단회원일 시"),
    대화전송_본인비활성("-11", "mailbox.chatroom.msg.send.me.inactive", "본인 비활성화 시"),
    대화전송_상대방비활성("-12", "mailbox.chatroom.msg.send.target.inactive", "상대방 비활성화 시"),
    대화전송_실패("C006", "mailbox.chatroom.msg.send.fail", "대화 전송 실패 시"),
    //대화읽음
    대화읽음_성공("0", "mailbox.chatroom.msg.read.success", "대화 읽음 성공 시"),
    대화읽음_회원아님("-1", "mailbox.chatroom.msg.read.member.number.error", "요청회원번호 회원 아닐 시"),
    대화읽음_채팅번호없음("-2", "mailbox.chatroom.msg.read.chat.number.error", "대화방번호 없을 시"),
    대화읽음_실패("C006", "mailbox.chatroom.msg.read.fail", "대화 읽음 실패 시"),
    //대화조회
    대화조회_성공("C001", "mailbox.chatroom.msg.select.success", "대화 조회 성공시"),
    대화조회_회원아님("-1", "mailbox.chatroom.msg.select.member.number.error", "요청회원번호 회원 아닐 시"),
    대화조회_채팅번호없음("-2", "mailbox.chatroom.msg.select.chat.number.error", "대화방번호 없을 시"),
    대화조회_실패("C006", "mailbox.chatroom.msg.select.fail", "대화 조회 실패 시"),
    //대화방 이미지조회
    대화방_이미지조회_성공("C001", "mailbox.chatroom.image.select.success", "대화방 이미지조회 성공 시"),
    대화방_이미지조회_회원아님("-1", "mailbox.chatroom.image.select.member.number.error", "요청회원번호 회원 아닐 시"),
    대화방_이미지조회_채팅번호없음("-2", "mailbox.chatroom.image.select.chat.number.error", "대화방번호 없을 시"),
    대화방_이미지조회_이미지타입아님("-3", "mailbox.chatroom.image.select.type.error", "이미지 타입 아닐 시"),
    대화방_이미지조회_삭제된이미지("-4", "mailbox.chatroom.image.select.already.delete", "이미 삭제된 이미지"),
    대화방_이미지조회_실패("C006", "mailbox.chatroom.image.select.fail", "대화방 이미지조회 실패 시"),
    //대화방 이미지 삭제
    대화방_이미지삭제_성공("0", "mailbox.chatroom.image.delete.success", "대화방 이미지 삭제 성공 시"),
    대화방_이미지삭제_회원아님("-1", "mailbox.chatroom.image.delete.member.number.error", "요청회원번호 회원 아닐 시"),
    대화방_이미지삭제_대화번호없음("-2", "mailbox.chatroom.image.delete.chat.number.error", "대화방번호 없을 시"),
    대화방_이미지삭제_이미지타입아님("-3", "mailbox.chatroom.image.delete.type.error", "이미지 타입 아닐 시"),
    대화방_이미지삭제_이미삭제됨("-4", "mailbox.chatroom.image.delete.already", "이미 삭제된 이미지"),
    대화방_이미지삭제_본인이미지아님("-5", "mailbox.chatroom.image.delete.not.me", "본인이미지 아닐 시"),
    대화방_이미지삭제_실패("C006", "mailbox.chatroom.image.delete.fail", "이미지 삭제 실패 시"),
    //대화방 알림조회
    대화방_알림조회_성공("0", "mailbox.chatroom.unread.success", "대화방 이미지 삭제 성공 시"),
    대화방_알림조회_회원아님("-1", "mailbox.chatroom.unread.member.number.error", "요청회원번호 회원 아닐 시"),
    대화방_알림조회_실패("C006", "mailbox.chatroom.unread.fail", "이미지 삭제 실패 시"),
    //메시지 활성화 설정
    활성화설정_성공("0", "mailbox.use.success", "활성화 설정 시"),
    활성화설정_ON("0", "mailbox.use.on.success", "활성화"),
    활성화설정_OFF("0", "mailbox.use.off.success", "비활성화"),
    활성화설정_회원아님("-1", "mailbox.use.member.number.error", "요청회원번호 회원 아닐 시"),
    활성화설정_실패("C006", "mailbox.use.fail", "활성화 설정 실패 시");

    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    MailBoxStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
