package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum MainStatus implements Status {


    //메인 팬 랭킹
    메인_팬랭킹조회_성공("C001", "main.fan.ranking.select.success", "팬 랭킹 조회 성공 시"),
    메인_팬랭킹조회_내역없음("0", "main.fan.ranking.no.ranking.success", "랭킹 내역 없을 시"),
    메인_팬랭킹조회_요청회원_회원아님("-1", "main.fan.ranking.member.number.error", "요청회원번호가 회원 아닐 시"),
    메인_팬랭킹조회_실패("C006", "main.fan.ranking.select.fail", "팬 랭킹 조회 실패 시"),
    //메인 DJ 랭킹
    메인_DJ랭킹조회_성공("C001", "main.dj.ranking.select.success", "DJ 랭킹 조회 성공 시"),
    메인_DJ랭킹조회_내역없음("0", "main.dj.ranking.no.ranking.success", "랭킹 내역 없을 시"),
    메인_DJ랭킹조회_요청회원_회원아님("-1", "main.dj.ranking.member.number.error", "요청회원번호가 회원 아닐 시"),
    메인_DJ랭킹조회_실패("C006", "main.dj.ranking.select.fail", "DJ 랭킹 조회 실패 시"),
    //메인 Level 랭킹
    메인_Level랭킹조회_성공("C001", "main.level.ranking.select.success", "Level 랭킹 조회 성공 시"),
    메인_Level랭킹조회_내역없음("0", "main.level.ranking.no.ranking.success", "랭킹 내역 없을 시"),
    메인_Level랭킹조회_실패("C006", "main.level.ranking.select.fail", "Level 랭킹 조회 실패 시"),
    //메인 마이DJ
    메인_마이DJ_조회성공("C001", "main.my.dj.select.success", "마이DJ 조회 성공 시"),
    메인_마이DJ_리스트없음("0", "main.my.dj.select.no.success", "마이DJ 내역 없을 시"),
    메인_마이DJ_요청회원_회원아님("-1", "main.my.dj.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    메인_마이DJ_조회실패("C006", "main.my.dj.select.fail", "마이DJ 조회 실패 시"),
    //메인 추천DJ 리스트
    메인_추천DJ리스트_조회성공("C001", "main.recommand.dj.select.success", "추천 DJ리스트 조회 성공 시"),
    메인_추천DJ리스트_없음("0", "main.recommand.dj.select.no.success", "추천 DJ리스트 없음"),
    메인_추천DJ리스트_조회실패("C006", "main.recommand.dj.select.fail", "추천 DJ리스트 조회 실패 시"),
    //메인 나의스타 리스트
    메인_나의스타_조회성공("C001", "main.star.dj.select.success", "나의스타 조회 성공 시"),
    메인_나의스타_없음("0", "main.star.dj.select.no.success", "나의스타 없음"),
    메인_나의스타_회원아님("-1", "main.star.dj.select.no.member", "회원아님"),
    메인_나의스타_조회실패("C006", "main.star.dj.select.fail", "나의스타 조회 실패 시"),
    //랭킹보상팝업 조회
    랭킹보상팝업조회_성공("0", "main.ranking.reward.select.success", "랭킹 보상 팝업조회 성공 시"),
    랭킹보상팝업조회_요청회원_회원아님("-1", "main.ranking.reward.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    랭킹보상팝업조회_TOP3_아님("-2", "main.ranking.reward.select.not.top3", "TOP3가 아닐 시"),
    랭킹보상팝업조회_보상테이블_없음("-3", "main.ranking.reward.select.no.reward.table", "보상테이블이 없을 시"),
    랭킹보상팝업조회_없는구분타입("-4", "main.ranking.reward.select.no.type", "없는 구분타입일 시"),
    랭킹보상팝업조회_실패("C006", "main.ranking.reward.select.fail", "랭킹 보상 팝업조회 실패 시"),
    //랜덤박스 열기
    랜덤박스열기_성공("0", "main.ranking.randombox.open.success", "랜덤박스 열기 성공 시"),
    랜덤박스열기_요청회원_회원아님("-1", "main.ranking.randombox.open.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    랜덤박스열기_TOP3_아님("-2", "main.ranking.randombox.open.not.top3", "TOP3가 아닐 시"),
    랜덤박스열기_보상대상_회원아님("-3", "main.ranking.randombox.open.reward.member.number.error", "보상대상 회원이 아닐 시"),
    랜덤박스열기_이미받음("-4", "main.ranking.randombox.open.already.reward", "이미 보상 받았을 시"),
    랜덤박스열기_없는구분타입("-5", "main.ranking.randombox.open.no.type", "없는 구분타입일 시"),
    랜덤박스열기_실패("C006", "main.ranking.randombox.open.fail", "랜덤박스 열기 실패 시"),
    //메인 랭킹
    메인_랭킹조회_성공("C001", "main.ranking.select.success", "랭킹 조회 성공 시"),
    메인_랭킹조회_내역없음("0", "main.ranking.no.ranking.success", "랭킹 내역 없을 시"),
    메인_랭킹조회_요청회원_회원아님("-1", "main.ranking.member.number.error", "요청회원번호가 회원 아닐 시"),
    메인_랭킹조회_실패("C006", "main.ranking.select.fail", "랭킹 조회 실패 시"),
    //메인 좋아요 랭킹
    메인_좋아요랭킹조회_성공("C001", "main.good.ranking.select.success", "좋아요 랭킹 조회 성공 시"),
    메인_좋아요랭킹조회_내역없음("0", "main.good.ranking.no.ranking.success", "좋아요 내역 없을 시"),
    메인_좋아요랭킹조회_실패("C006", "main.good.ranking.select.fail", "좋아요 랭킹 조회 실패 시"),
    //어워드DJ리스트
    어워드DJ조회_성공("C001", "award.dj.list.success", "어워드 DJ조회 성공 시"),
    어워드DJ조회_회원아님("-1", "award.dj.list.member.number.error", "요청회원번호 회원 아닐 시"),
    어워드DJ조회_실패("C006", "award.dj.list.fail", "어워드 DJ조회 실패 시"),
    //어워드 DJ투표
    투표_성공("0", "award.vote.success", "투표 성공 시"),
    투표_회원아님("-1", "award.vote.member.number.error", "요청회원번호 회원 아닐 시"),
    투표_이미함("-2", "award.vote.already", "투표 이미한 경우"),
    투표_10레벨미만("-3", "award.vote.level.limit.10", "10레벨 미만일 경우"),
    투표_동일번호("-4", "award.vote.same.phone.number", "동일 휴대폰번호 중복 투표시"),
    투표_실패("C006", "award.vote.fail", "투표 실패 시"),
    //투표결과조회
    투표결과조회_성공("C001", "award.vote.result.success", "투표결과 조회 성공 시"),
    투표결과조회_회원아님("-1", "award.vote.result.member.number.error", "요청회원번호 회원 아닐 시"),
    투표결과조회_실패("C006", "award.vote.result.fail", "투표결과 조회 실패 시"),
    //타임랭킹조회
    메인_타임랭킹조회_성공("C001", "main.time.rank.select.success", "타임랭킹 조회 시"),
    메인_타임랭킹조회_요청회원_회원아님("-1", "main.time.rank.select.member.number.error", "요청회원번호 회원 아닐 시"),
    메인_타임랭킹조회_실패("C006", "main.time.rank.select.fail", "타임랭킹 조회 실패 시"),
    // 와썹맨
    와썹맨_DJ_리스트("C001", "whats.up.dj.list", "와썹맨_DJ_리스트"),
    와썹맨_DJ_조회("C002", "whats.up.dj.sel", "와썹맨_DJ_조회"),
    와썹맨_신입_리스트("C003", "whats.up.new.member.list", "와썹맨_신입_리스트"),
    와썹맨_신입_조회("C004", "whats.up.new.member.sel", "와썹맨_신입_조회"),
    와썹맨_회차_조회("C005", "whats.up.info.sel", "와썹맨_회차_조회"),
    와썹맨_DJ_리스트_파라미터("EP001", "whats.up.dj.list.parameter", "와썹맨_DJ_리스트_파라미터"),
    와썹맨_DJ_조회_파라미터("EP002", "whats.up.dj.sel.parameter", "와썹맨_DJ_조회_파라미터"),
    와썹맨_신입_리스트_파라미터("EP003", "whats.up.new.member.list.parameter", "와썹맨_신입_리스트_파라미터"),
    와썹맨_신입_조회_파라미터("EP004", "whats.up.new.member.sel.parameter", "와썹맨_신입_조회_파라미터"),
    와썹맨_회차_조회_파라미터("EP005", "whats.up.info.sel.parameter", "와썹맨_회차_조회_파라미터"),
    // 베스트DJ들 팬랭킹 조회
    베스트DJ팬랭킹조회_성공("0", "best.fan.rank.list.success", "베스트DJ들 팬랭킹 조회 성공 시"),
    베스트DJ팬랭킹조회_실패("C006", "best.fan.rank.list.fail", "베스트DJ들 팬랭킹 조회 실패 시");

    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    MainStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
