package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum EventStatus implements Status {

    출석체크이벤트_상태조회_실패_회원아님("-1", "event.attendance.check.fail.no.member", "요청 회원번호가 없음"),
    출석체크이벤트_상태조회_실패("C006", "business.error", "비즈니스 에러"),
    출석체크이벤트_상태조회_성공("0", "read.success", "성공"),
    출석체크이벤트_출석_성공("0", "event.attendance.checkIn.success", "성공"),
    출석체크이벤트_출석_실패_회원아님("-1", "event.attendance.checkIn.fail.no.member", "요청 회원번호 없음"),
    출석체크이벤트_출석_실패_이미받음("-2", "event.attendance.checkIn.fail.already", "이미 받음"),
    출석체크이벤트_출석_실패_필요시간부족("-3", "event.attendance.checkIn.fail.short.time", "필요시간부족"),
    출석체크이벤트_출석_실패_보상테이블없음("-4", "event.attendance.checkIn.fail.table.emapty", "보상테이블없음"),
    출석체크이벤트_출석_실패_동일기기중복불가("-5", "event.attendance.checkIn.fail.device.check", "동일기기 중복불가"),
    출석체크이벤트_출석_실패_동일아이피중복불가("-6", "event.attendance.checkIn.fail.ip.check", "동일아이피 중복불가"),
    출석체크이벤트_출석_실패("C006", "business.error", "비즈니스 에러"),
    출석체크이벤트_더줘_성공("0", "event.attendance.checkIn.success", "성공"),
    출석체크이벤트_더줘_실패_회원아님("-1", "event.attendance.checkIn.fail.no.member", "요청 회원번호 없음"),
    출석체크이벤트_더줘_실패_이미받음("-2", "event.attendance.bonus.fail.already", "이미 받음"),
    출석체크이벤트_더줘_실패_대상아님("-3", "event.attendance.bonus.no.auth", "대상아님"),
    출석체크이벤트_더줘_실패("C006", "business.error", "비즈니스 에러"),
    설정_방생성_참여불가상태("S001", "system.config.broadcast.block", "tbl_code_defind -> system_config / 방생성_참여_가능여부 가 Y 일경우"),
    설정_클립업로드_참여불가상태("S001", "system.config.clip.block", "tbl_code_defind -> system_config / 클립_업로드_가능여부 가 Y 일경우"),
    라이징이벤트_실시간순위_조회_성공("C001", "event.rising.live.success", "성공"),
    라이징이벤트_실시간순위_데이터없음("0", "event.rising.live.no.data", "데이터 없음"),
    라이징이벤트_결과_조회_성공("C001", "event.rising.result.success", "성공"),
    라이징이벤트_결과_데이터없음("0", "event.rising.result.no.data", "데이터 없음"),
    이벤트_참여날짜아님("-1", "event.join.invalid.date", "이벤트 기간이 아닌경우"),
    이벤트_이미참여("-2", "event.join.already", "이미 이벤트에 참여한 경우"),
    이벤트_진행중인이벤트없음("-3", "event.current.not.exist", "진행중인 이벤트가 없는경우"),
    이벤트_잘못된경품번호("-3", "event.gift.not.found", "이벤트 경품번호를 입력한 경우"),
    이벤트_응모권수량부족("-4", "event.coupon.not.enough", "응모권 갯수가 부족한 경우"),
    이벤트_방송시간부족("-2", "event.broadcast.time.not.enough", "방송시간이 조건에 미달된 경우"),
    이벤트_참여성공("0", "event.apply.reward.success", "이벤트 참여 완료"),
    //랭킹 이벤트 실시간 순위보기
    랭킹이벤트실시간순위리스트조회("C001", "event.ranking.live.list.select.success", "랭킹이벤트실시간순위리스트 정보 조회 성공 시"),
    랭킹이벤트실시간순위리스트없음("0", "event.ranking.live.list.no.success", "랭킹이벤트실시간순위리스트가 없을 시"),
    랭킹이벤트실시간순위리스트_실패("C006", "event.ranking.live.list.select.fail", "랭킹이벤트실시간순위리스트 정보 조회 실패 시"),
    //랭킹 이벤트 결과보기
    랭킹이벤트결과조회("C001", "event.ranking.result.list.select.success", "랭킹이벤트결과 정보 조회 성공 시"),
    랭킹이벤트결과없음("0", "event.ranking.result.list.no.success", "랭킹이벤트결과가 없을 시"),
    랭킹이벤트결과_실패("C006", "event.ranking.result.list.select.fail", "랭킹이벤트결과 정보 조회 실패 시"),
    //이벤트 댓글 리스트 조회
    이벤트댓글리스트조회("C001", "event.reply.result.list.select.success", "이벤트댓글리스트 정보 조회 성공 시"),
    이벤트댓글리스트없음("0", "event.reply.result.list.no.success", "이벤트댓글리스트가 없을 시"),
    이벤트댓글리스트_실패("C006", "event.reply.result.list.select.fail", "이벤트댓글리스트 정보 조회 실패 시"),
    //이벤트 댓글 달기
    이벤트_댓글달기성공("1", "event.reply.add.success", "이벤트 댓글달기 성공 시"),
    이벤트_댓글달기실패_회원아님("C006", "event.reply.add.no.member", "회원아님"),
    이벤트_댓글달기실패_등록오류("C006", "event.reply.add.error", "이벤트 댓글 등록 실패"),
    이벤트_댓글달기실패_이벤트종료("C006", "event.reply.end", "이벤트 댓글 이벤트 마감"),
    //이벤트 댓글 삭제
    이벤트_댓글삭제성공("C001", "event.reply.remove.success", "이벤트 댓글 삭제 성공 시"),
    이벤트_댓글삭제실패_삭제권한없음("-1", "event.reply.remove.authorization.not", "삭제 권한이 없음"),
    이벤트_댓글삭제정보없음("0", "event.reply.remove.no.data", "이벤트 댓글 삭제 정보가 없을시"),
    이벤트_댓글삭제실패_등록오류("C006", "event.reply.remove.error", "이벤트 댓글 삭제 실패"),
    //출석완료체크
    출석완료체크_성공("0", "event.attendance.check.success", "출석완료 체크 성공 시"),
    출석완료체크_회원아님("-1", "event.attendance.check.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    출석완료체크_실패("C006", "event.attendance.check.fail", "출석완료 체크 실패 시"),
    //휴대폰입력 (출석체크 이벤트)
    휴대폰입력_성공("0", "event.attendance.phone.input.success", "휴대폰 입력 저장 성공 시"),
    휴대폰입력_회원아님("-1", "event.attendance.phone.input.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    휴대폰입력_당첨자아님("-2", "event.attendance.phone.input.prize.no.member", "당첨자가 아닐 시"),
    휴대폰입력_자리수11미만("-3", "event.attendance.phone.input.number.limit.11", "번호 11자리 미만일 시"),
    휴대폰입력_입력종료시간지남("-4", "event.attendance.phone.input.limit.time", "입력종료시간 지났을 시"),
    휴대폰입력_이미입력된번호("-5", "event.attendance.phone.input.already", "이미 입력된 번호일 시"),
    휴대폰입력_실패("C006", "event.attendance.phone.input.fail", "휴대폰 입력 저장 실패 시"),
    //출석체크
    출석체크예약체크_팝업노출("0", "read.success", "자동출석체크 예약팝업 띄우기"),
    출석체크예약_회원아님("-1", "event.attendance.check.member.number.error", "회원정보를 찾을 수 없을 때"),
    출석체크예약체크_이미팝업띄움("-2", "read.success", "하루 1번 노출 정책으로 이미 팝업을 띄운경우"),
    출석체크예약_예약성공("1", "event.attendance.checkIn.reserve.success", "출석체크예약 성공"),
    출석체크예약_보상받음("0", "event.attendance.checkIn.success", "출석체크보상 받기 성공"),
    출석체크예약_이미보상받음("-2", "event.attendance.checkIn.fail.already", "출석체크보상을 이미 받은경우"),
    출석체크예약_동일기기중복불가("-5", "event.attendance.checkIn.fail.device.check", "출석체크 시 UUID 중복된 경우"),
    출석체크예약_동일아이피중복불가("-6", "event.attendance.checkIn.fail.ip.check", "출석체크 시 IP가 중복된 경우"),
    //기프티콘 당첨자리스트 조회
    기프티콘_당첨자리스트조회("C001", "gifticon.win.list.select.success", "기프티콘 당첨자 조회 성공 시"),
    기프티콘_당첨자리스트없음("0", "gifticon.win.list.select.no.success", "당첨자 없을 시"),
    기프티콘_당첨자리스트조회_회원아님("-1", "gifticon.win.list.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    기프티콘_당첨자리스트조회_실패("C006", "gifticon.win.list.select.fail", "기프티콘 당첨자 조회 실패 시"),
    //이벤트 체크/참여
    이벤트_체크_참여("0", "event.enable.success", "참여 가능"),
    이벤트_참여("0", "event.apply.success", "참여 완료"),
    이벤트_체크_이미참여("1", "event.already.apply", "이미 참여"),
    이벤트_체크_자격안됨("2", "event.lacking.auth", "자격조건 안됨"),
    이벤트_체크_자격안됨04("2", "event.lacking.auth.04", "자격조건 안됨"),
    이벤트_없음_종료("-7", "event.not.found", "이벤트 없거나 종료"),
    이벤트_비회원("-8", "event.not.member", "미회원"),
    이벤트_에러("-9", "event.error", "오류"),
    노하우_이벤트_회원아님("-1", "event.not.member", "미회원"),
    노하우_이벤트_이벤트없음("-2", "event.not.found", "존재하지 않는 이벤트"),
    노하우_이벤트_좋아요("0", "event.good.success", "이벤트 좋아요 성공"),
    노하우_이벤트_좋아요취소("1", "event.good.cancel.success", "이벤트 좋아요 취소 성공"),
    //이벤트 관리 페이지
    이벤트_리스트조회_성공("C001", "event.page.list.success", "이벤트 리스트 조회 성공 시"),
    이벤트_리스트조회_데이터없음("0", "event.page.no.data", "이벤트 리스트 조회 시 데이터가 없을 때"),
    이벤트_리스트조회_실패("-9", "business.error", "이벤트 리스트 조회 실패 시"),
    이벤트_당첨자명단조회_성공("C001", "event.page.win.list.success", "이벤트 당첨자 명단 조회 성공 시"),
    이벤트_당첨자명단조회_이벤트번호없음("-1", "event.page.no.eventIdx", "이벤트 당첨자 명단 조회 시 이벤트 번호가 없을 때"),
    이벤트_당첨자명단조회_결과없음("-2", "event.page.not.complete", "이벤트 당첨자 선정이 완료가 안됐을 시"),
    이벤트_당첨자명단조회_실패("-9", "business.error", "이벤트 당첨자 명단 조회 실패 시"),
    이벤트_당첨여부조회_성공("C001", "event.page.win.result.success", "이벤트 당첨 여부 조회 성공 시"),
    이벤트_당첨여부조회_당첨자아님("0", "event.page.not.winner", "이벤트 당첨 여부 조회 시 당첨자가 아닐 때"),
    이벤트_당첨여부조회_회원아님("-1", "event.page.not.member", "이벤트 당첨 여부 조회 시 회원 번호가 없을 때"),
    이벤트_당첨여부조회_이벤트번호없음("-2", "event.page.no.eventIdx", "이벤트 당첨 여부 조회 시 이벤트 번호가 없을 때"),
    이벤트_당첨여부조회_결과없음("-3", "event.page.not.complete", "이벤트 당첨자 선정이 완료가 안됐을 시"),
    이벤트_당첨여부조회_실패("-9", "business.error", "이벤트 당첨 여부 조회 실패 시"),
    이벤트_경품수령방법입력_경품받기_성공("1", "event.prize.receive.choice.prize.success", "이벤트 경품 배송수령 입력 성공 시"),
    이벤트_경품수령방법입력_달로받기_성공("2", "event.prize.receive.choice.dal.success", "이벤트 경품 달로 받기 입력 성공 시"),
    이벤트_경품수령방법입력_회원아님("-1", "event.page.not.member", "이벤트 경품 수령 방법 입력 시 회원 번호가 없을 때"),
    이벤트_경품수령방법입력_이벤트번호없음("-2", "event.page.no.eventIdx", "이벤트 경품 수령 방법 입력 시 이벤트 번호가 없을 때"),
    이벤트_경품수령방법입력_결과없음("-3", "event.page.not.complete", "이벤트 당첨자 선정이 완료가 안됐을 시"),
    이벤트_경품수령방법입력_당첨자아님("-4", "event.page.not.winner", "이벤트 경품 수령 방법 입력 시 당첨자가 아닐 때"),
    이벤트_경품수령방법입력_이미경품선택함_입력불가능("-5", "event.prize.receive.choice.already.choice", "배송 정보 입력 후 새로 배송 정보를 입력하려할 시(달로 받기 선택은 가능)"),
    이벤트_경품수령방법입력_수령방법_오류("-6", "event.prize.receive.choice.receiveWay.error", "이벤트 경품 수령 방법이 (1,2)가 아닐 시"),
    이벤트_경품수령방법입력_입금확인후_수정불가능("-7", "event.prize.receive.choice.already.deposit", "경품 입금 확인 후 수령 방법 수정 불가능할 때"),
    이벤트_경품수령방법입력_실패("-9", "business.error", "이벤트 경품 수령 방법 입력 실패 시"),
    이벤트_당첨자등록정보조회_성공("0", "event.winner.add.info.select.success", "이벤트 당첨자 등록정보 조회 성공 시"),
    이벤트_당첨자등록정보조회_회원아님("-1", "event.page.not.member", "이벤트 당첨자 등록정보 조회 시 회원 번호가 없을 때"),
    이벤트_당첨자등록정보조회_이벤트번호없음("-2", "event.page.no.eventIdx", "이벤트 당첨자 등록정보 조회 시 이벤트 번호가 없을 때"),
    이벤트_당첨자등록정보조회_결과없음("-3", "event.page.not.complete", "이벤트 당첨자 선정이 완료가 안됐을 시"),
    이벤트_당첨자등록정보조회_당첨자아님("-4", "event.page.not.winner", "이벤트 당첨자 등록정보 조회 시 당첨자가 아닐 때"),
    이벤트_당첨자등록정보조회_경품번호없음("-5", "event.page.no.prizeIdx", "이벤트 당첨자 등록정보 조회 시 경품 번호가 없을 때"),
    이벤트_당첨자등록정보조회_실패("-9", "business.error", "이벤트 당첨자 등록정보 조회 실패 시"),
    이벤트_당첨자등록정보수정_등록성공("0", "event.winner.add.info.insert.success", "이벤트 당첨자 등록정보 최초 등록 성공 시"),
    이벤트_당첨자등록정보수정_성공("1", "event.winner.add.info.edit.success", "이벤트 당첨자 등록정보 수정 성공 시"),
    이벤트_당첨자등록정보수정_회원아님("-1", "event.page.not.member", "이벤트 당첨자 등록정보 수정 시 회원 번호가 없을 때"),
    이벤트_당첨자등록정보수정_이벤트번호없음("-2", "event.page.no.eventIdx", "이벤트 당첨자 등록정보 수정 시 이벤트 번호가 없을 때"),
    이벤트_당첨자등록정보수정_결과없음("-3", "event.page.not.complete", "이벤트 당첨자 선정이 완료가 안됐을 시"),
    이벤트_당첨자등록정보수정_당첨자아님("-4", "event.page.not.winner", "이벤트 당첨자 등록정보 수정 시 당첨자가 아닐 때"),
    이벤트_당첨자등록정보수정_입금확인후_수정불가능("-5", "event.winner.add.info.edit.already.deposit", "경품 입금 확인 후 수령 방법 수정 불가능할 때"),
    이벤트_당첨자등록정보수정_경품번호없음("-6", "event.page.no.prizeIdx", "이벤트 당첨자 등록정보 수정 시 경품 번호가 없을 때"),
    이벤트_당첨자등록정보수정_실패("-9", "business.error", "이벤트 당첨자 등록정보 조회 실패 시"),
    //추석이벤트 체크
    추석이벤트체크_참여가능("0", "event.chooseok.check.ok.success", "추석이벤트 참여가능"),
    추석이벤트체크_회원아님("-1", "event.chooseok.check.member.number.error", "요청회원번호가 회원 아닐 시"),
    추석이벤트체크_이미받음("-2", "event.chooseok.check.already", "추석이벤트 이미 지급 받음"),
    추석이벤트체크_참여기간아님("-3", "event.chooseok.check.invalid.date", "추석이벤트 참여 기간 아님"),
    추석이벤트체크_실패("C006", "event.chooseok.check.fail", "추석이벤트 참여 실패"),
    //추석 무료 달 지급 이벤트
    추석이벤트_무료달지급_지급성공("0", "event.chooseok.free.dal.success", "추석이벤트 무료 달 지급 성공 시"),
    추석이벤트_무료달지급_회원아님("-1", "event.chooseok.check.member.number.error", "요청회원번호가 회원 아닐 시"),
    추석이벤트_무료달지급_이미지급받음("-2", "event.chooseok.check.already", "추석이벤트 무료달 이미 지급받았을 시"),
    추석이벤트_무료달지급_레벨5미만("-3", "event.chooseok.free.dal.level.not.enough", "추석이벤트 무료 달 지급 레벨에 못 미칠시"),
    추석이벤트_무료달지급_이벤트종료("-4", "event.chooseok.free.dal.end", "추석이벤트 무료 달 지급 이벤트가 끝났을 시"),
    추석이벤트_무료달지급_실패("C006", "event.chooseok.free.dal.fail", "추석이벤트 무료 달 지급 실패 시"),
    //추석이벤트 구매 달 확인
    추석이벤트_구매달조회_성공("0", "event.chooseok.purchaseDal.select.success", "추석이벤트 구매 달 조회 성공 시"),
    추석이벤트_구매달조회_회원아님("-1", "event.chooseok.check.member.number.error", "요청회원번호가 회원 아닐 시"),
    추석이벤트_구매달조회_실패("C006", "event.chooseok.purchaseDal.select.fail", "추석이벤트 구매달 조회 실패 시"),
    //추석이벤트 구매 달 보너스 지급
    추석이벤트_보너스달지급_지급성공("0", "event.chooseok.purchaseDal.bonus.success", "추석이벤트 구매 달 별 보너스 지급 성공 시"),
    추석이벤트_보너스달지급_회원아님("-1", "event.chooseok.check.member.number.error", "요청회원번호가 회원 아닐 시"),
    추석이벤트_보너스달지급_이미지급받음("-2", "event.chooseok.purchaseDal.already", "추석이벤트 구매 달 별 보너스 이미 지급받았을 시"),
    추석이벤트_보너스달지급_지급기간이아님("-3", "event.chooseok.purchaseDal.bonus.not.yet", "추석이벤트 구매 달 별 보너스 지급 기간이 아닐 시"),
    추석이벤트_보너스달지급_이벤트종료("-4", "event.chooseok.purchaseDal.bonus.end", "추석이벤트 구매 달 별 보너스 지급 이벤트가 끝났을 시"),
    추석이벤트_보너스달지급_500달미만("-5", "event.chooseok.purchaseDal.bonus.dal.not.enough", "추석이벤트 구매 달 별 보너스"),
    추석이벤트_보너스달지급_지급실패("C006", "event.chooseok.purchaseDal.bonus.fail", "추석이벤트 구매 달 별 보너스 지급 실패 시"),
    //응모권조회(룰렛이벤트)
    응모권조회_성공("0", "event.roulette.coupon.select.success", "응모권 조회 성공 시"),
    응모권조회_회원아님("-1", "event.roulette.coupon.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    응모권조회_실패("C006", "event.roulette.coupon.select.fail", "응모권 조회 실패 시"),
    //룰렛 스타트(룰렛이벤트)
    스타트_성공("0", "event.roulette.start.success", "룰렛 스타트 성공 시"),
    스타트_회원아님("-1", "event.roulette.start.member.number.error", "요청회원번호가 회원 아닐 시"),
    스타트_응모권부족("-2", "event.roulette.start.coupon.limit", "응모권 부족 시"),
    스타트_실패("C006", "event.roulette.start.fail", "룰렛 스타트 성공 시"),
    //전화번호 입력(룰렛이벤트)
    전화번호입력_성공("0", "event.roulette.input.phone.success", "입력 성공 시"),
    전화번호입력_회원아님("-1", "event.roulette.input.phone.member.number.error", "요청회원번호가 회원 아닐 시"),
    전화번호입력_당첨내역없음("-2", "event.roulette.input.phone.no.prize", "당첨내역 없을 시"),
    전화번호입력_자리수이상("-3", "event.roulette.input.phone.error", "전화번호 자리수 오류 시"),
    전화번호입력_실패("C006", "event.roulette.input.phone.fail", "입력 실패 시"),
    //참여 이력 조회(룰렛이벤트)
    참여이력조회_성공("C001", "event.roulette.apply.select.success", "참여 이력 조회 성공 시"),
    참여이력조회_없음("0", "event.roulette.apply.select.no", "참여 이력 없을 시"),
    참여이력조회_회원아님("-1", "event.roulette.apply.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    참여이력조회_실패("C006", "event.roulette.apply.select.fail", "참여 이력 조회 성공 시"),
    //당첨자 조회(룰렛이벤트)
    당첨자조회_성공("C001", "event.roulette.win.select.success", "당첨자 조회 성공 시"),
    당첨자조회_없음("0", "event.roulette.win.select.no", "당첨자 조회 없을 시"),
    당첨자조회_실패("C006", "event.roulette.win.select.fail", "당첨자 조회 실패 시"),
    //보름달 띄우기
    슈퍼문_완성("2", "super.moon.complete.success", "슈퍼문 완성 시"),
    보름달_완성("1", "moon.complete.success", "보름달 완성 시"),
    보름달_미완성("0", "moon.no.complete.success", "보름달 미완성 시"),
    보름달_회원아님("-1", "moon.member.number.error", "요청회원번호 회원 아닐 시"),
    보름달_방번호_오류("-2", "moon.room.number.error", "방 번호 오류 시"),
    보름달_종료된방("-3", "moon.room.end", "종료된 방일 시"),
    보름달_청취자아님("-4", "moon.not.room.member", "청취자가 아닐 시"),
    보름달_이미완성("-5", "moon.already", "보름달 이미 완성 시"),
    보름달_실패("C006", "moon.fail", "보름달 실패 시"),
    //보름달 체크
    보름달체크_방번호_오류("-1", "moon.check.room.number.error", "방 번호 오류 시"),
    보름달체크_종료된방("-2", "moon.check.room.end", "종료된 방일 시"),
    보름달체크_이미완성("-3", "moon.check.complete.already", "보름달 이미 완성 시"),
    //가입이벤트 팝업노출
    오레벨노출_성공("5", "join.event.check.5.show.success", "5레벨 배너, 팝업노출 시"),
    십레벨노출_성공("10", "join.event.check.10.show.success", "10레벨 배너, 팝업노출 시"),
    노출_회원아님("-1", "join.event.check.member.number.error", "요청회원번호 회원 아닐 시"),
    노출_대상아님("-2", "join.event.check.no.show", "배너, 팝업노출 대상 아닐 시"),
    노출_실패("C006", "join.event.check.fail", "배너, 팝업노출 체크 실패 시"),
    //가입이벤트 상세보기
    가입이벤트_상세보기_성공("0", "join.event.detail.success", "가입이벤트 상세보기 시"),
    가입이벤트_상세보기_회원아님("-1", "join.event.detail.member.number.error", "요청회원번호 회원 아닐 시"),
    가입이벤트_상세보기_실패("C006", "join.event.detail.fail", "가입이벤트 상세보기 실패 시"),
    //가입이벤트 보상받기
    가입이벤트_보상받기_성공("0", "join.event.reward.success", "보상받기 성공 시"),
    가입이벤트_보상받기_회원아님("-1", "join.event.reward.member.number.error", "요청회원번호 회원 아닐 시"),
    가입이벤트_보상받기_레벨부족("-2", "join.event.reward.level.limit", "보상받기 레벨부족 시"),
    가입이벤트_보상받기_기간종료("-3", "join.event.reward.date.end", "보상받기 기간종료 시"),
    가입이벤트_보상받기_이미받음("-4", "join.event.reward.already", "보상받기 이미 받았을 시"),
    가입이벤트_보상받기_본인인증안됨("-5", "join.event.reward.auth.no", "보상받기 본인인증 안됨"),
    가입이벤트_보상받기_본인인증번호_이미보상받음("-6", "join.event.reward.auth.already", "이미 본인인증번호로 보상받음"),
    가입이벤트_보상받기_실패("C006", "join.event.reward.fail", "보상받기 실패 시"),
    //일간 최고 DJ/FAN 조회
    일간최고조회_성공("C001", "open.event.daily.best.select.success", "일간 최고 DJ/FAN 조회 성공 시"),
    일간최고조회_실패("C006", "open.event.daily.best.select.fail", "일간 최고 DJ/FAN 조회 실패 시"),
    //퀘스트 목록 조회
    퀘스트목록조회_성공("0", "event.quest.list.select.success", "퀘스트 목록 조회 성공 시"),
    퀘스트목록조회_회원아님("-1", "event.quest.list.select.member.number.error", "퀘스트 목록 조회 회원 아닐 시"),
    퀘스트목록조회_실패("C006", "event.quest.list.select.fail", "퀘스트 목록 조회 실패 시"),
    // 퀘스트 보상받기
    퀘스트보상_성공("0", "event.quest.reward.success", "퀘스트 보상 성공 시"),
    퀘스트보상_회원아님("-1", "event.quest.reward.select.member.number.error", "퀘스트 보상 회원 아닐 시"),
    퀘스트보상_조건미달("-2", "event.quest.reward.auth.under", "퀘스트 보상 조건 미달 시"),
    퀘스트보상_이미받음("-3", "event.quest.reward.error", "퀘스트 보상 이미 받은 보상 시"),
    퀘스트보상_번호없음("-4", "event.quest.reward.no.search", "퀘스트 보상 번호 없을 시"),
    퀘스트보상_실패("C006", "event.quest.reward.fail", "퀘스트 보상 실패 시"),
    // 1주년 이벤트 콜백
    댓글목록조회_성공("0", "one.year.event.list.success", "1주년 이벤트 댓글목록조회 성공 시"),
    댓글목록조회_실패("C006", "one.year.event.list.fail", "1주년 이벤트 댓글목록조회 실패 시"),
    댓글등록_성공("0", "one.year.event.tail.upload.success", "1주년 이벤트 댓글 등록 성공 시"),
    이벤트댓글등록_금지어("C005", "event.tail.ban.word", "댓글 금지어 포함 시"),
    댓글등록_실패("C006", "one.year.event.tail.upload.fail", "1주년 이벤트 댓글 등록 실패 시"),
    댓글수정_성공("0", "one.year.event.tail.update.success", "1주년 이벤트 댓글 수정 성공 시"),
    댓글수정_실패("C006", "one.year.event.tail.update.fail", "1주년 이벤트 댓글 수정 실패 시"),
    댓글삭제_성공("0", "one.year.event.tail.delete.success", "1주년 이벤트 댓글 삭제 성공 시"),
    댓글삭제_실패("C006", "one.year.event.tail.delete.fail", "1주년 이벤트 댓글 삭제 실패 시"),
    달지급조회_성공("0", "one.year.event.dal.check.success", "1주년 이벤트 달 지급 조회 성공 시"),
    달지급조회_이미받음("-1", "one.year.event.dal.check.already", "1주년 이벤트 달 지급 조회 이미 받았을 시"),
    달지급조회_레벨안됨("-2", "one.year.event.dal.check.low", "1주년 이벤트 달 지급 조회 레벨 안될 시"),
    달지급조회_인증안됨("-3", "one.year.event.dal.check.auth", "1주년 이벤트 달 지급 조회 인증 안될 시"),
    달지급조회_실패("C006", "one.year.event.dal.check.fail", "1주년 이벤트 달 지급 조회 실패 시"),
    달지급처리및로그기록_성공("0", "one.year.event.ins.dal.log.success", "1주년 이벤트 달 지급 처리 및 로그기록 성공 시"),
    달지급처리및로그기록_이미받음("-1", "one.year.event.ins.dal.log.already", "1주년 이벤트 달 지급 처리 및 로그기록 이미 받았을 시"),
    달지급처리및로그기록_레벨안됨("-2", "one.year.event.ins.dal.log.low", "1주년 이벤트 달 지급 처리 및 로그기록 레벨 안될 시"),
    달지급처리및로그기록_인증안됨("-3", "one.year.event.ins.dal.log.auth", "1주년 이벤트 달 지급 처리 및 로그기록 인증 안될 시"),
    달지급처리및로그기록_실패("C006", "one.year.event.ins.dal.log.fail", "1주년 이벤트 달 지급 처리 및 로그기록 실패 시"),
    //스톤 모으기
    달라_이벤트_조각부족("C001", "dalla.event.bbobgi.ins.fail", "달라 조각 부족"),
    // 공유 이벤트
    공유이벤트_댓글목록_조회_성공("0", "share.event.tail.list.success", "공유 이벤트 댓글 목록 조회 성공 시"),
    공유이벤트_댓글목록_조회_실패("-1", "share.event.tail.list.fail", "공유 이벤트 댓글 목록 조회 실패 시"),
    공유이벤트_댓글목록_삭제_성공("0", "share.event.tail.del.success", "공유 이벤트 댓글 목록 삭제 성공 시"),
    공유이벤트_댓글목록_삭제_실패("-1", "share.event.tail.del.fail", "공유 이벤트 댓글 목록 삭제 실패 시"),
    공유이벤트_댓글목록_등록_성공("0", "share.event.tail.ins.success", "공유 이벤트 댓글 목록 등록 성공 시"),
    공유이벤트_댓글목록_등록_실패("-1", "share.event.tail.ins.fail", "공유 이벤트 댓글 목록 등록 실패 시"),
    공유이벤트_댓글목록_수정_성공("0", "share.event.tail.upd.success", "공유 이벤트 댓글 목록 수정 성공 시"),
    공유이벤트_댓글목록_수정_실패("-1", "share.event.tail.upd.fail", "공유 이벤트 댓글 목록 수정 실패 시"),
    공유이벤트_댓글여부_체크_성공("0", "share.event.tail.chk.success", "공유 이벤트 댓글 체크 성공 시"),
    공유이벤트_댓글여부_체크_실패("-2", "share.event.tail.chk.fail", "공유 이벤트 댓글 체크 실패 시"),
    //오픈이벤트
    오픈이벤트조회_성공("C001", "open.event.select.success", "오픈이벤트 조회 성공 시"),
    오픈이벤트조회_실패("C006", "open.event.select.fail", "오픈이벤트 조회 실패 시"),
    // 달나라
    달나라_팝업조회_성공("0", "moonLand.mission.sel.success", "달나라 팝업 조회 성공 시"),
    달나라_팝업조회_실패("C006", "moonLand.mission.sel.fail", "달나라 팝업 조회 실패 시"),
    달나라_점수등록_성공("0", "moonLand.mission.score.ins.success", "달나라 점수 등록 성공 시"),
    달나라_점수등록_실패("C006", "moonLand.mission.score.ins.fail", "달나라 점수 등록 실패 시"),
    //스페셜리그 조회
    스페셜리그_조회_성공("C001", "special.league.select.success", "스페셜리그 조회 시"),
    스페셜리그_조회_기수오버("-2", "special.league.select.round.over", "현재보다 큰 기수 조회 시"),
    스페셜리그_조회_실패("C006", "special.league.select.fail", "스페셜리그 조회 실패 시"),
    //챔피언십 조회
    챔피언십조회_성공("C001", "championship.event.select.success", "챔피언십 조회 성공 시"),
    챔피언십조회_실패("C006", "championship.event.select.fail", "챔피언십 조회 실패 시"),
    //챔피언십 승점 조회
    챔피언십_승점조회_성공("C001", "championship.point.event.select.success", "챔피언십 승점조회 성공 시"),
    챔피언십_승점조회_실패("C006", "championship.point.event.select.fail", "챔피언십 승점조회 실패 시"),
    //챔피언십 선물받기
    선물받기_성공("0", "championship.gift.success", "선물받기 성공 시"),
    선물받기_회원아님("-1", "championship.gift.member.number.error", "요청회원번호 회원 아닐 시"),
    선물받기_이미받음("-2", "championship.gift.already", "이미 받은 경우"),
    선물받기_10점안됨("-3", "championship.gift.10point.limit", "10점 미만인 경우"),
    선물받기_실패("C006", "championship.gift.fail", "선물받기 실패 시"),
    // 달라그라운드 조회
    달라그라운드조회_성공("C001", "dallaGround.event.select.success", "달라그라운드 조회 성공 시"),
    달라그라운드조회_실패("C006", "dallaGround.event.select.fail", "달라그라운드 조회 실패 시"),
    // 달라그라운드 내순위 조회
    달라그라운드_내순위_조회_성공("1", "dallaGround.my.rank.select.success", "달라그라운드 내 순위 조회 성공 시"),
    달라그라운드_내순위_조회_실패("-1", "dallaGround.my.rank.select.fail", "달라그라운드 내 순위 조회 실패 시"),
    달라그라운드_내순위_조회_팀없음("-2", "dallaGround.my.rank.select.team.no.fail", "달라그라운드 내 순위 조회 팀번호 조회 실패 시"),
    //전광판 이벤트
    전광판_디제이_조회_성공("0", "electric.sign.event.dj.success", "Dj 목록 조회 성공 시"),
    전광판_디제이_조회_실패("C006", "electric.sign.event.dj.fail", "Dj 목록 조회 실패 시"),
    전광판_시청자_조회_성공("0", "electric.sign.event.fan.success", "시청자 목록 조회 성공 시"),
    전광판_시청자_조회_실패("C006", "electric.sign.event.fan.fail", "시청자 목록 조회 실패 시");




    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    EventStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
