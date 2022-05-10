package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum ExchangeStatus implements Status {

    //환전계산
    환전계산성공("0", "exchange.calc.success", "환전 계산 성공 시"),
    환전계산_회원아님("-1", "exchange.calc.no.member","요청회원번호 회원 아닐 시"),
    환전계산_별체크("-2", "exchange.calc.limit.byeol", "최소 별 570 이상 아닐 시"),
    환전계산실패("C006", "exchange.calc.fail", "환전 계산 실패 시"),
    //환전신청
    환전신청성공("0", "exchange.apply.success", "환전 신청 성공 시"),
    환전신청_회원아님("-1", "exchange.apply.no.member", "요청회원번호 회원 아닐 시"),
    환전신청_별체크("-2", "exchange.apply.limit.byeol", "최소 별 570 이상 아닐 시"),
    환전신청_예금주오류("-3", "exchange.apply.account.name.error", "예금주 오류 시"),
    환전신청_은행코드오류("-4", "exchange.apply.bank.code.error", "은행코드 오류 시"),
    환전신청_계좌번호오류("-5", "exchange.apply.account.no.error", "계좌번호 오류 시"),
    환전신청_주민번호오류("-6", "exchange.apply.social.no.error", "주민번호 오류 시"),
    환전신청_전화번호오류("-7", "exchange.apply.phone.no.error", "전화번호 오류 시"),
    환전신청_주소1오류("-8", "exchange.apply.address1.no.error", "주소1 오류 시"),
    환전신청_첨부파일1오류("-9", "exchange.apply.addfile1.no.error", "첨부파일1 오류 시"),
    환전신청_첨부파일2오류("-10", "exchange.apply.addfile2.no.error", "첨부파일2 오류 시"),
    환전신청_동의오류("-11", "exchange.apply.agree.no.error", "동의 오류 시"),
    환전신청_별부족("-12", "exchange.apply.byeol.lack", "별 부족 시"),
    환전신청_신청제한("-13", "exchange.apply.limit", "환전 신청 횟수 초과 시"),
    환전신청_기존신청정보오류("-14", "exchange.apply.already.addfile.error", "기존 신청정보 오류 시"),
    환전신청실패("C006", "exchange.apply.fail", "환전 신청 실패 시"),
    //환전승인건조회
    환전승인조회성공("0", "exchange.approval.list.select.success", "환전 승인 건 조회 성공 시"),
    환전승인조회없음("-1", "exchange.approval.list.select.none", "환전 승인 건 없을 시"),
    //계좌등록
    계좌등록_성공("C001", "exchange.my.account.number.add.success", "환전계좌 등록 성공 시"),
    계좌등록_이미등록됨("-1", "exchange.my.account.number.add.already", "환전계좌 이미 등록 되었을 시"),
    계좌등록_실패("C006", "exchange.my.account.number.add.fail", "환전계좌 등록 실패 시"),
    //계좌수정
    계좌수정_성공("C001", "exchange.my.account.number.edit.success", "환전계좌 수정 성공 시"),
    계좌수정_실패("C006", "exchange.my.account.number.edit.fail", "환전계좌 수정 실패 시"),
    //계좌삭제
    계좌삭제_성공("C001", "exchange.my.account.number.delete.success", "환전계좌 삭제 성공 시"),
    계좌삭제_실패("C006", "exchange.my.account.number.delete.fail", "환전계좌 삭제 실패 시"),
    //계좌조회
    계좌조회_성공("C001", "exchange.my.account.number.select.success", "환전계좌 내역 조회 성공 시"),
    계좌조회_없음("0", "exchange.my.account.number.select.no", "환전계좌 내역 조회 없을 시"),
    //계좌 수정&삭제 불가
    계좌수정_불가("-1", "exchange.my.account.number.edit.impossible", "계좌 수정 불가 시"),
    계좌삭제_불가("-1", "exchange.my.account.number.delete.impossible", "계좌 삭제 불가 시"),
    //환전 취소하기
    환전취소_성공("0", "exchange.cancel.success", "환전 취소 성공 시"),
    환전취소_회원아님("-1", "exchange.cancel.member.number.error", "요청회원번호 회원 아닐 시"),
    환전취소_환전번호없음("-2", "exchange.cancel.exchange.idx.error", "환전 신청 번호 없을 시"),
    환전취소_미처리상태아님("-3", "exchange.cancel.state.error", "미처리 상태가 아닐 시"),
    환전취소_취소불가시간("-4", "exchange.cancel.time.impossible", "취소 불가 시간일 경우"),
    환전취소_대상불일지("-5", "exchange.cancel.member.error", "환전신청 대상 불일치일 경우"),
    환전취소_이미취소됨("-6", "exchange.cancel.already", "이미 취소된 경우"),
    환전취소_실패("C006", "exchange.cancel.fail", "환전 취소 실패 시");


    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    ExchangeStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
