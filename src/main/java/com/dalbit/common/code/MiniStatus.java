package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum MiniStatus implements Status {
    //미니게임 조회
    미니게임_조회_성공("C001", "mini.game.select.success", "미니게임 조회 성공 시"),
    미니게임_조회_회원아님("-1", "mini.game.select.member.number.error", "요청회원번호 회원 아닐 시"),
    미니게임_조회_해당방없음("-2", "mini.game.select.no.room", "해당 방송이 없을 시"),
    미니게임_조회_방이종료됨("-3", "mini.game.select.end.room", "방이 종료되어 있을 시"),
    미니게임_조회_실패("C006", "mini.game.select.fail", "미니게임 조회 실패 시"),
    //미니게임 등록
    미니게임등록_성공("0", "mini.game.add.success", "미니게임 룰렛 등록 시"),
    미니게임등록_회원아님("-1", "mini.game.add.member.number.error", "요청회원번호 회원 아닐 시"),
    미니게임등록_방번호없음("-2", "mini.game.add.no.room", "해당 방송이 없을 시"),
    미니게임등록_종료된방("-3", "mini.game.add.end.room", "방이 종료되어 있을 시"),
    미니게임등록_방장아님("-4", "mini.game.add.no.bj", "방장이 아닐 시"),
    미니게임등록_이미등록("-5", "mini.game.add.already", "미니게임 룰렛이 이미 등록되었을 시"),
    미니게임등록_금액설정오류("-6", "mini.game.add.amount.err", "금액설정 범위 오류 시"),
    미니게임등록_옵션개수오류("-7", "mini.game.add.option.count.err", "옵션 개수 오류 시"),
    미니게임등록_옵션리스트오류("-8", "mini.game.add.option.list.err", "옵션 리스트 오류 시"),
    미니게임등록_실패("C006", "mini.game.add.fail", "미니게임 룰렛 등록 실패 시"),
    //미니게임 수정
    미니게임수정_성공("0", "mini.game.edit.success", "미니게임 수정 시"),
    미니게임수정_룰렛("0", "mini.game.edit.roulette.success", "미니게임 룰렛 수정 시"),
    미니게임수정_회원아님("-1", "mini.game.edit.member.number.error", "요청회원번호 회원 아닐 시"),
    미니게임수정_방번호없음("-2", "mini.game.edit.no.room", "해당 방송이 없을 시"),
    미니게임수정_종료된방("-3", "mini.game.edit.end.room", "방이 종료되어 있을 시"),
    미니게임수정_방장아님("-4", "mini.game.edit.no.bj", "방장이 아닐 시"),
    미니게임수정_없음("-5", "mini.game.edit.no", "등록된 미니게임 룰렛이 없을 시"),
    미니게임수정_금액설정오류("-6", "mini.game.edit.amount.err", "금액설정 범위 오류 시"),
    미니게임수정_옵션개수오류("-7", "mini.game.edit.option.count.err", "옵션 개수 오류 시"),
    미니게임수정_옵션리스트오류("-8", "mini.game.edit.option.list.err", "옵션 리스트 오류 시"),
    미니게임수정_실패("C006", "mini.game.edit.fail", "미니게임 룰렛 수정 실패 시"),
    //미니게임 조회
    미니게임조회_성공("0", "mini.game.select.success", "미니게임 룰렛 조회 시"),
    미니게임조회_회원아님("-1", "mini.game.select.member.number.error", "요청회원번호 회원 아닐 시"),
    미니게임조회_해당방없음("-2", "mini.game.select.no.room", "해당 방송이 없을 시"),
    미니게임조회_방이종료됨("-3", "mini.game.select.end.room", "방이 종료되어 있을 시"),
    미니게임조회_무료상태("-4", "mini.game.select.not.free", "무료상태 방장만 가능"),
    미니게임조회_없음("-5", "mini.game.select.no", "등록된 미니게임 룰렛이 없을 시"),
    미니게임조회_실패("C006", "mini.game.select.fail", "미니게임 룰렛 조회 실패 시"),
    //미니게임 시작
    미니게임시작_성공("0", "mini.game.start.success", "미니게임 시작 시"),
    미니게임시작_회원아님("-1", "mini.game.start.member.number.error", "요청회원번호 회원 아닐 시"),
    미니게임시작_해당방없음("-2", "mini.game.start.no.room", "해당 방송이 없을 시"),
    미니게임시작_방이종료됨("-3", "mini.game.start.end.room", "방이 종료되어 있을 시"),
    미니게임시작_청취자아님("-4", "mini.game.start.not.in.member", "해당방에 청취중이 아닐 시"),
    미니게임시작_없음("-5", "mini.game.start.no", "등록된 미니게임이 없을 시"),
    미니게임시작_무료상태("-6", "mini.game.start.not.free", "무료상태 방장만 가능"),
    미니게임시작_수정버전오류("-7", "mini.game.start.version.mismatch", "수정버전과 일치하지 않을 경우"),
    미니게임시작_보유달부족("-8", "mini.game.start.dal.limit", "보유 달 부족 시"),
    미니게임시작_진행중("-11", "mini.game.start.ing", "미니게임 진행중"),
    미니게임시작_실패("C006", "mini.game.start.fail", "미니게임 시작 실패 시"),
    //미니게임 종료
    미니게임종료_성공("0", "mini.game.end.success", "미니게임 룰렛 종료 시"),
    미니게임종료_회원아님("-1", "mini.game.end.member.number.error", "요청회원번호 회원 아닐 시"),
    미니게임종료_방번호없음("-2", "mini.game.end.no.room", "해당 방송이 없을 시"),
    미니게임종료_종료된방("-3", "mini.game.end.end.room", "방이 종료되어 있을 시"),
    미니게임종료_방장아님("-4", "mini.game.end.not.bj", "방장이 아닐 시"),
    미니게임종료_없음("-5", "mini.game.end.no", "등록된 미니게임 룰렛이 없을 시"),
    미니게임종료_실패("C006", "mini.game.end.fail", "미니게임 룰렛 종료 실패 시"),
    // 룰렛 당첨내역 조회
    룰렛당첨내역_조회_성공("0", "mini.game.win.list.success", "미니게임 룰렛 당첨내역 조회 성공"),
    룰렛당첨내역_조회_회원아님("-1", "mini.game.select.member.number.error", "요청회원번호 회원 아닐 시"),
    룰렛당첨내역_조회_해당방없음("-2", "mini.game.select.no.room", "해당 방송이 없을 시"),
    룰렛당첨내역_조회_방이종료됨("-3", "mini.game.select.end.room", "방이 종료되어 있을 시"),
    룰렛당첨내역_조회_방장다름("-4", "mini.game.win.list.diffDj", "요청 회원이 방장이 아닐 시"),
    룰렛당첨내역_조회_실패("-5", "mini.game.win.list.fail", "미니게임 룰렛 당첨내역 조회 실패");

    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    MiniStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
