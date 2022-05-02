package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum CommonStatus implements Status {
    //공통
    생성금지("C100", "create.no", "방생성 금지"),
    공백("TEMP", "empty.success", "공백"),
    조회("C001", "read.success", "조회"),
    수정("C002", "update.success", "수정"),
    생성("C003", "create.success", "생성"),
    삭제("C004", "delete.success", "삭제"),
    파라미터오류("C005", "param.error", "파라미터 오류 시"),
    비즈니스로직오류("C006", "business.error", "비즈니스로직 오류 시"),
    벨리데이션체크("C007", "validation.error", "벨리데이션체크 오류 시"),
    부적절한문자열("C007", "string.error", "부적합한 기호 및 문자열 포함 시"),
    데이터없음("0", "no.data", "데이터가 없을 시"),
    로그인필요_성공("-98", "need.login.success", "로그인 필요 시"),
    로그인필요("-99", "need.login", "로그인 필요 시"),
    이전작업대기중("-97", "ready.to.prev.process", "동일작업 중복 호출"),
    사용자요청취소("C999", "client.abort.exception", "사용자 요청취소"),
    최신버전_업데이트_필요("100", "update.need", "업데이트 필요"),
    //에러 로그 저장
    에러로그저장_성공("0", "error.log.save.success", "에러 로그 저장 성공 시"),
    에러로그저장_실패("C006", "error.log.save.fail", "에러 로그 저장 실패 시"),
    //푸시 등록
    푸시등록_성공("0","insert.push.success", "푸시등록 성공 시"),
    푸시등록_에러("C006", "server.error", "푸시등록 실패 시"),
    //푸시 발송
    푸시성공("0", "push.add.success", "푸시 성공 시"),
    푸시_회원아님("-1", "push.add.member.number.error", "회원 번호 아닐 시"),
    푸시_디바이스토큰없음("-2", "push.add.device.token.no", "디바이스 토큰 없을 시"),
    푸시실패("C006", "push.add.fail", "푸시 실패 시"),
    // 푸시 클릭
    푸시클릭성공("0", "push.click.success", "푸시 클릭 성공 시"),
    푸시클릭_푸시번호없음("-1", "push.click.push.idx.error", "PUSH 번호 미존재 시"),
    푸시클릭_에러("C006", "server.error", "푸시 클릭 실패 시"),
    //금지어 체크
    닉네임금지("C005", "nick.name.ban.word", "닉네임 금지어 포함 시"),
    방송방생성제목금지("C005", "broadcast.create.title.ban.word", "방송방 생성 제목 금지어 포함 시"),
    방송방생성인사말금지("C005", "broadcast.create.welcome.msg.ban.word", "방송방 생성 인사말 금지어 포함 시"),
    방송방수정제목금지("C005", "broadcast.edit.title.ban.word", "방송방 수정 제목 금지어 포함 시"),
    방송방수정인사말금지("C005", "broadcast.edit.welcome.msg.ban.word", "방송방 수정 인사말 금지어 포함 시"),
    클립등록제목금지("C005", "clip.add.title.ban.word", "클립 생성 제목 금지어 포함 시"),
    미니게임생성_옵션금지("C005", "mini.game.add.option.ban.word", "미니게임 옵션 금지어 포함 시"),
    //이모티콘 조회
    이모티콘조회_성공("0", "emoticon.select.success", "이모티콘 조회 성공 시"),
    이모티콘조회_회원아님("-1", "emoticon.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    이모티콘조회_실패("C006", "emoticon.select.fail", "이모티콘 조회 실패 시"),
    이모티콘조회_없음("C007", "emoticon.select.no", "이모티콘 없을 시"),
    카테고리_없음("C008", "emoticon.category.select.no", "카테고리 없을 시"),
    //문자발송
    문자발송_성공("0", "sms.send.success", "문자 발송 성공 시"),
    문자발송_실패("C006", "sms.send.fail", "문자 발송 실패 시"),
    //랭킹반영설정
    랭킹반영("0", "rank.setting.on.success", "랭킹 반영 시"),
    랭킹미반영("0", "rank.setting.off.success", "랭킹 미반영 시"),
    랭킹반영설정_회원아님("-1", "rank.setting.member.number.error", "요청회원번호 회원 아닐 시"),
    랭킹반영설정_실패("C006", "rank.setting.fail", "랭킹 설정 실패 시"),
    //명예의전당 조회
    명예의전당_조회_성공("C001", "award.honor.select.success", "명예의 전당 조회 시"),
    명예의전당_조회_실패("C006", "award.honor.select.fail", "명예의 전당 조회 실패 시"),
    //알림받기 등록/해제
    알림_등록("0", "recv.on.success", "알림 등록 시"),
    알림_해제("0", "recv.off.success", "알림 해제 시"),
    알림_회원아님("-1", "recv.member.number.error", "요청회원번호 회원 아닐 시"),
    알림_실패("C006", "recv.fail", "랭킹 설정 실패 시"),
    //알림회원 삭제
    알림회원삭제_성공("0", "recv.delete.success", "알림회원 삭제 시"),
    알림회원삭제_회원아님("-1", "recv.delete.member.number.error", "요청회원번호 회원 아닐 시"),
    알림회원삭제_실패("C006", "recv.delete.fail", "알림회원 삭제 실패 시"),
    //알림회원조회
    알림회원조회_성공("C001", "recv.select.success", "알림회원 조회 시"),
    알림회원조회_실패("C006", "recv.select.fail", "알림회원 조회 실패 시"),
    //공통
    공통_기본_성공("C001", "common.select.success", "기본 조회 시"),
    공통_기본_요청회원_정보없음("-1", "common.select.member.number.error", "memNo 없음"),
    공통_기본_실패("C006", "common.select.fail", "기본 조회 실패 시"),
    공통_기본_DB_실패("C007", "common.select.fail", "기본 조회 실패 db null"),
    아이피_조회("C001", "common.nation.sel", "아이피_조회"),
    아이피_조회_결과없음("C002", "common.nation.sel.none", "아이피_조회_결과없음"),
    // 앱 보안 로그 저장
    앱보안_로그저장_성공("0", "app.security.log.save.success", "앱 보안 로그 저장 성공 시"),
    앱보안_로그저장_실패("C006", "app.security.log.save.fail", "앱 보안 로그 저장 실패 시");


    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    CommonStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
