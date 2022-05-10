package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum GuestStatus implements Status {

    //게스트 신청
    게스트신청_성공("0", "guest.propose.success", "게스트 신청 성공 시"),
    게스트신청_회원아님("-1", "guest.propose.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트신청_방번호없음("-2", "guest.propose.no.room.number", "방번호 없을 시"),
    게스트신청_종료된방번호("-3", "guest.propose.end.room.number", "종료된 방 번호일 시"),
    게스트신청_청취자아님("-4", "guest.propose.no.room.member", "방 청취자 아닐 시"),
    게스트신청_신청불가상태("-5", "guest.propose.no.state", "신청 불가 상태 시"),
    게스트신청_이미신청중("-6", "guest.propose.already", "이미 신청중일 시"),
    게스트신청_초대받은상태("-7", "guest.propose.already.invite.success", "이미 초대받은 상태 시"),
    게스트신청_실패("C006", "guest.propose.fail", "게스트 신청 실패 시"),
    //게스트 신청 취소
    게스트신청취소_성공("0", "guest.propose.cancel.success", "게스트 신청 취소 성공 시"),
    게스트신청취소_회원아님("-1", "guest.propose.cancel.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트신청취소_방번호없음("-2", "guest.propose.cancel.no.room.number", "방번호 없을 시"),
    게스트신청취소_종료된방번호("-3", "guest.propose.cancel.end.room.number", "종료된 방 번호일 시"),
    게스트신청취소_청취자아님("-4", "guest.propose.cancel.no.room.member", "방 청취자 아닐 시"),
    게스트신청취소_불가상태("-5", "guest.propose.cancel.no.state", "취소 불가 상태 시"),
    게스트신청취소_신청상태아님("-6", "guest.propose.cancel.no.propose.state", "신청상태가 아닐 시"),
    게스트신청취소_실패("C006", "guest.propose.cancel.fail", "게스트 신청 취소 실패 시"),
    //게스트 관리 조회
    게스트관리조회_성공("C001", "guest.management.select.success", "게스트 관리 조회 성공 시"),
    게스트리스트_없음("0", "guest.management.select.no.success", "게스트 리스트 없을 시"),
    게스트관리조회_요청회원번호_회원아님("-1", "guest.management.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트관리조회_방번호없음("-2", "guest.management.select.no.room.number", "방번호 없을 시"),
    게스트관리조회_종료된방번호("-3", "guest.management.select.end.room.number", "종료된 방 번호일 시"),
    게스트관리조회_청취자아님("-4", "guest.management.select.no.room.member", "방 청취자 아닐 시"),
    게스트관리조회_권한없음("-5", "guest.management.select.no.auth", "조회 권한 없을 시"),
    게스트관리조회_실패("C006", "guest.management.select.fail", "게스트 관리 조회 실패 시"),
    //게스트 초대
    게스트초대_성공("0", "guest.invite.success", "게스트 초대 성공 시"),
    게스트초대_회원아님("-1", "guest.invite.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트초대_초대대상_회원아님("-2", "guest.invite.object.member.number.error", "초대대상 회원번호가 정상회원이 아닐 시"),
    게스트초대_방번호없음("-3", "guest.invite.no.room.number", "방번호 없을 시"),
    게스트초대_종료된방번호("-4", "guest.invite.end.room.number", "종료된 방 번호일 시"),
    게스트초대_요청회원_방에없음("-5", "guest.invite.room.join.no", "요청회원 방에 없을 시"),
    게스트초대_권한없음("-6", "guest.invite.no.auth", "초대 권한 없을 시"),
    게스트초대_초대회원_방에없음("-7", "guest.invite.object.room.join.no", "초대 대상이 방에 없을 시"),
    게스트초대_초대불가("-8", "guest.invite.impossible", "대상 초대불가일 시"),
    게스트초대_이미초대중("-10", "guest.invite.already", "대상 이미 초대중일 시"),
    게스트초대_이미존재_제한("-11", "guest.invite.already.guest", "게스트가 이미 존재중일 시"),
    게스트초대_다른회원_초대중("-12", "guest.invite.another.member.inviting", "다른회원 초대중일 시"),
    게스트초대_실패("C006", "guest.invite.fail", "게스트 초대 실패 시"),
    //게스트 초대 취소
    게스트초대취소_성공("0", "guest.invite.cancel.success", "게스트 초대 성공 시"),
    게스트초대취소_회원아님("-1", "guest.invite.cancel.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트초대취소_초대대상_회원아님("-2", "guest.invite.cancel.object.member.number.error", "초대대상 회원번호가 정상회원이 아닐 시"),
    게스트초대취소_방번호없음("-3", "guest.invite.cancel.no.room.number", "방번호 없을 시"),
    게스트초대취소_종료된방번호("-4", "guest.invite.cancel.end.room.number", "종료된 방 번호일 시"),
    게스트초대취소_요청회원_방에없음("-5", "guest.invite.cancel.room.join.no", "요청회원 방에 없을 시"),
    게스트초대취소_권한없음("-6", "guest.invite.cancel.no.auth", "초대 취소 권한 없을 시"),
    게스트초대취소_초대회원_방에없음("-7", "guest.invite.cancel.object.room.join.no", "초대 대상이 방에 없을 시"),
    게스트초대취소_초대취소불가("-8", "guest.invite.cancel.impossible", "대상 초대 취소불가일 시"),
    게스트초대취소_초대중아님("-10", "guest.invite.cancel.no.inviting", "대상 초대중 아닐 시"),
    게스트초대취소_실패("C006", "guest.invite.cancel.fail", "게스트 초대 실패 시"),
    //게스트 초대 수락
    게스트초대수락_성공("0", "guest.invite.ok.success", "게스트 초대 수락 성공 시"),
    게스트초대수락_회원아님("-1", "guest.invite.ok.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트초대수락_방번호없음("-2", "guest.invite.ok.no.room.number", "방번호 없을 시"),
    게스트초대수락_종료된방번호("-3", "guest.invite.ok.end.room.number", "종료된 방 번호일 시"),
    게스트초대수락_요청회원_방에없음("-4", "guest.invite.ok.room.join.no", "요청회원 방에 없을 시"),
    게스트초대수락_불가("-5", "guest.invite.ok.impossible", "대상 초대 수락불가일 시"),
    게스트초대수락_초대상태아님("-6", "guest.invite.no.inviting", "대상 초대중 아닐 시"),
    게스트초대수락_실패("C006", "guest.invite.ok.fail", "게스트 초대 수락 실패 시"),
    //게스트 초대 거절
    게스트초대거절_성공("0", "guest.invite.refuse.success", "게스트 거절 성공 시"),
    게스트초대거절_회원아님("-1", "guest.invite.refuse.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트초대거절_초대대상_회원아님("-2", "guest.invite.refuse.object.member.number.error", "거절 대상 회원번호가 정상회원이 아닐 시"),
    게스트초대거절_방번호없음("-3", "guest.invite.refuse.no.room.number", "방번호 없을 시"),
    게스트초대거절_종료된방번호("-4", "guest.invite.refuse.end.room.number", "종료된 방 번호일 시"),
    게스트초대거절_요청회원_방에없음("-5", "guest.invite.refuse.room.join.no", "요청회원 방에 없을 시"),
    게스트초대거절_권한없음("-6", "guest.invite.refuse.no.auth", "거절 취소 권한 없을 시"),
    게스트초대거절_초대회원_방에없음("-7", "guest.invite.refuse.object.room.join.no", "거절 대상이 방에 없을 시"),
    게스트초대거절_초대취소불가("-8", "guest.invite.refuse.impossible", "대상 거절 취소불가일 시"),
    게스트초대거절_초대중아님("-10", "guest.invite.refuse.no.inviting", "대상 초대중 아닐 시"),
    게스트초대거절_실패("C006", "guest.invite.refuse.fail", "게스트 거절 실패 시"),
    //게스트리스트조회
    게스트리스트조회_성공("C001", "guest.select.success", "게스트 리스트 조회 성공 시"),
    게스트리스트조회_없음("0", "guest.select.no", "게스트 리스트 없을 시"),
    게스트리스트조회_실패("C006", "guest.select.fail", "게스트 리스트 조회 실패 시");

    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    GuestStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
