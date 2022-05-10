package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum MypageStatus implements Status {


    //회원 달 선물하기
    달선물_성공("0", "mypage.member.gift.dal.success", "달 선물 성공 시"),
    달선물_요청회원번호_회원아님("-1", "mypage.member.gift.dal.member.no", "요청회원번호가 회원이 아닐 시"),
    달선물_받는회원번호_회원아님("-2", "mypage.member.gift.dal.object.member.no", "받는회원번호가 회원이 아닐 시"),
    달선물_달개수_비정상("-3", "mypage.member.gift.dal.count.error", "선물하는 달 개수가 비정상일 시"),
    달선물_달개수_부족("-4", "mypage.member.gift.dal.limit", "달 개수가 부족할 시"),
    달선물_실패("C006", "mypage.member.gift.dal.fail", "달 선물 실패 시"),
    //회원 알림 내용 조회
    회원알림내용조회_성공("C001", "mypage.member.notification.select.success", "회원 알림내용 조회 성공 시"),
    회원알림내용조회_알림없음("0", "mypage.member.notification.select.no.success", "알림 없을 시"),
    회원알림내용조회_회원아님("-1", "mypage.member.notification.select.not.member", "회원이 아닐 시"),
    회원알림내용조회_실패("C006", "mypage.member.notification.select.fail", "회원 알림내용 조회 실패 시"),
    //회원 알림 삭제
    회원알림삭제_성공("0", "mypage.member.notification.delete.success", "회원 알림내용 삭제 성공 시"),
    회원알림내용삭제_회원아님("-1", "mypage.member.notification.delete.not.member", "회원이 아닐 시"),
    회원알림내용삭제_실패("C006", "mypage.member.notification.delete.fail", "회원 알림내용 삭제 실패 시"),
    //회원 닉네임 검색
    회원닉네임검색_성공("C001", "member.nick.search.success", "회원 닉네임 검색 성공 시"),
    회원닉네임검색_결과없음("0", "member.nick.search.no.success", "결과 없을 시"),
    회원닉네임검색_실패("C006", "member.nick.search.fail", "회원 닉네임 검색 실패 시"),
    //마이페이지 공지사항 등록
    공지등록_성공("0", "mypage.notice.create.success", "마이페이지 공지등록 성공 시"),
    공지등록_요청회원번호_회원아님("-1", "mypage.notice.create.member.number.error", "요청회원번호가 회원 아닐 시"),
    공지등록_대상회원번호_회원아님("-2", "mypage.notice.create.object.member.number.error", "대상회원번호가 회원 아닐 시"),
    공지등록_권한없음("-3", "mypage.notice.create.authorization.not", "공지등록 권한 없을 시"),
    공지등록_상단고정_초과("-4", "mypage.notice.create.topFix.fail", "마이페이지 공지등록 실패 시"),
    공지등록_실패("C006", "mypage.notice.create.fail", "마이페이지 공지등록 실패 시"),
    공지이미지_경로변경_실패("-1", "mypage.notice.photo.move.fail", "포토서버 이미지 경로변경 실패"),
    //마이페이지 공지사항 수정
    공지수정_성공("0", "mypage.notice.edit.success", "마이페이지 공지수정 성공 시"),
    공지수정_요청회원번호_회원아님("-1", "mypage.notice.edit.member.number.error", "요청회원번호가 회원 아닐 시"),
    공지수정_대상회원번호_회원아님("-2", "mypage.notice.edit.object.member.number.error", "대상회원번호가 회원 아닐 시"),
    공지수정_권한없음("-3", "mypage.notice.edit.authorization.not", "공지수정 권한 없을 시"),
    공지수정_잘못된공지번호("-4", "mypage.notice.edit.number.error", "공지번호가 잘못된 번호일 시"),
    공지수정_실패("C006", "mypage.notice.edit.fail", "마이페이지 공지수정 실패 시"),
    공지수정_사진작업_실패("C006", "mypage.notice.edit.photo.fail", "마이페이지 공지수정 실패 시"),
    //마이페이지 공지사항 삭제
    공지삭제_성공("0", "mypage.notice.delete.success", "마이페이지 공지삭제 성공 시"),
    공지삭제_요청회원번호_회원아님("-1", "mypage.notice.delete.member.number.error", "요청회원번호가 회원 아닐 시"),
    공지삭제_대상회원번호_회원아님("-2", "mypage.notice.delete.object.member.number.error", "대상회원번호가 회원 아닐 시"),
    공지삭제_권한없음("-3", "mypage.notice.delete.authorization.not", "공지삭제 권한 없을 시"),
    공지삭제_잘못된공지번호("-4", "mypage.notice.delete.number.error", "공지번호가 잘못된 번호일 시"),
    공지삭제_실패("C006", "mypage.notice.delete.fail", "마이페이지 공지삭제 실패 시"),
    //마이페이지 공지사항 조회수
    공지조회수_성공("0", "mypage.notice.read.success", "마이페이지 공지조회 성공 시"),
    공지조회수_회원아님("-1", "mypage.notice.read.member.number.error", "회원번호가 회원 아닐 시"),
    공지조회수_잘못된공지번호("-2", "mypage.notice.read.number.error", "공지번호가 잘못된 번호일 시"),
    공지조회수_작성자_조회("-3", "mypage.notice.read.writer.error", "공지 작성자 조회 시"),
    공지조회수_이미조회("-4", "mypage.notice.read.already.error", "공지 중복 조회 시"),
    공지조회수_실패("C006", "mypage.notice.read.fail", "마이페이지 공지 조회 실패 시"),
    //마이페이지 공지사항 조회
    공지조회_성공("C001", "mypage.notice.select.success", "마이페이지 공지조회 성공 시"),
    공지조회_없음("0", "mypage.notice.select.no.success", "공지사항 없을 시"),
    공지조회_요청회원번호_회원아님("-1", "mypage.notice.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    공지조회_대상회원번호_회원아님("-2", "mypage.notice.select.object.member.number.error", "대상회원번호가 회원 아닐 시"),
    공지조회_실패("C006", "mypage.notice.select.fail", "마이페이지 공지조회 실패 시"),
    //마이페이지 내지갑 달 사용내역 조회
    달사용내역조회_성공("C001", "mypage.wallet.dal.select.success", "달 사용내역 조회 성공 시"),
    달사용내역조회_없음("0","mypage.wallet.dal.select.no.success", "달 사용내역 없을 시"),
    달사용내역조회_요청회원번호_회원아님("-1","mypage.wallet.dal.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    달사용내역조회_실패("C006", "mypage.wallet.dal.select.fail", "달 사용내역 조회 실패 시"),
    //마이페이지 내지갑 별 사용내역 조회
    별사용내역조회_성공("C001", "mypage.wallet.byeol.select.success", "별 사용내역 조회 성공 시"),
    별사용내역조회_없음("0","mypage.wallet.byeol.select.no.success", "별 사용내역 없을 시"),
    별사용내역조회_요청회원번호_회원아님("-1","mypage.wallet.byeol.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    별사용내역조회_실패("C006", "mypage.wallet.byeol.select.fail", "별 사용내역 조회 실패 시"),
    //마이페이지 방송내역 조회
    방송내역조회_성공("C001", "mypage.broadcast.report.select.success", "방송내역 조회 성공 시"),
    방송내역조회_없음("0","mypage.broadcast.report.select.no.success", "방송내역 없을 시"),
    방송내역조회_요청회원번호_회원아님("-1","mypage.broadcast.report.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    방송내역조회_실패("C006", "mypage.broadcast.report.select.fail", "방송내역 조회 실패 시"),
    //마이페이지 청취내역 조회
    청취내역조회_성공("C001", "mypage.listen.report.select.success", "청취내역 조회 성공 시"),
    청취내역조회_없음("0","mypage.listen.report.select.no.success", "청취내역 없을 시"),
    청취내역조회_요청회원번호_회원아님("-1","mypage.listen.report.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    청취내역조회_실패("C006", "mypage.listen.report.select.fail", "청취내역 조회 실패 시"),
    //마이페이지 방송설정 금지어 조회
    금지어조회_성공("0", "mypage.broadcast.ban.word.select.success", "금지어 조회 성공 시"),
    금지어조회_요청번호_회원아님("-1", "mypage.broadcast.ban.word.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    금지어조회_실패("C006", "mypage.broadcast.ban.word.select.fail", "금지어 조회 실패 시"),
    //마이페이지 방송설정 금지어 저장
    금지어저장_성공("0", "mypage.broadcast.ban.word.save.success", "금지어 저장 성공 시"),
    금지어저장_요청번호_회원아님("-1", "mypage.broadcast.ban.word.save.member.number.error", "요청회원번호가 회원 아닐 시"),
    금지어저장_초과("-2", "mypage.broadcast.ban.word.save.limit", "금지어 저장 100개 초과 시"),
    금지어저장_실패("C006", "mypage.broadcast.ban.word.save.fail", "금지어 저장 실패 시"),
    //마이페이지 방송설정 유저 검색
    유저검색_성공("C001", "mypage.broadcast.search.user.success", "유저 검색 성공 시"),
    유저검색_없음("0", "mypage.broadcast.search.user.no.success", "검색 내역 없을 시"),
    유저검색_요청회원번호_회원아님("-1", "mypage.broadcast.search.user.member.number.error","요청회원번호가 회원 아닐 시"),
    유저검색_실패("C006", "mypage.broadcast.search.user.fail", "유저 검색 실패 시"),
    //마이페이지 방송설정 고정매니저 조회
    고정매니저조회_성공("C001", "mypage.broadcast.manager.select.success", "고정매니저 조회 성공 시"),
    고정매니저조회_없음("0", "mypage.broadcast.manager.select.no.success", "조회 내역 없을 시"),
    고정매니저조회_요청회원번호_회원아님("-1", "mypage.broadcast.manager.select.member.number.error","요청회원번호가 회원 아닐 시"),
    고정매니저조회_실패("C006", "mypage.broadcast.manager.select.fail", "고정매니저 조회 실패 시"),
    //마이페이지 방송설정 고정매니저 등록
    고정매니저등록_성공("0", "mypage.broadcast.manager.add.success", "고정매니저 등록 성공 시"),
    고정매니저등록_요청회원번호_회원아님("-1", "mypage.broadcast.manager.add.member.number.error", "요청회원번호가 회원 아닐 시"),
    고정매니저등록_매니저회원번호_회원아님("-2", "mypage.broadcast.manager.add.number.error", "매니저 회원번호가 회원 아닐 시"),
    고정매니저등록_5명초과("-3", "mypage.broadcast.manager.add.five.people.limit", "5명초과 등록 안될 시"),
    고정매니저등록_이미매니저등록("-4", "mypage.broadcast.manager.add.already.add", "이미 매니저로 등록 되어있을 시"),
    고정매니저등록_실패("C006", "mypage.broadcast.manager.add.fail", "고정매니저 등록 실패 시"),
    //마이페이지 방송설정 고정매니저 권한 수정
    고정매니저_권한수정_성공("0", "mypage.broadcast.manager.edit.success", "고정매니저 권한수정 성공 시"),
    고정매니저_권한수정_요청회원번호_회원아님("-1", "mypage.broadcast.manager.edit.member.number.error", "요청회원번호가 회원 아닐 시"),
    고정매니저_권한수정_매니저회원번호_회원아님("-2", "mypage.broadcast.manager.edit.number.error", "매니저 회원번호가 회원 아닐 시"),
    고정매니저_권한수정_등록된매니저아님("-3", "mypage.broadcast.manager.edit.add.manager", "등록된 매니저가 아닐 시"),
    고정매니저_권한수정_실패("C006", "mypage.broadcast.manager.edit.fail", "고정매니저 권한수정 실패 시"),
    //마이페이지 방송설정 고정매니저 해제
    고정매니저해제_성공("0", "mypage.broadcast.manager.delete.success", "고정매니저 해제 성공 시"),
    고정매니저해제_요청회원번호_회원아님("-1", "mypage.broadcast.manager.delete.member.number.error", "요청회원번호가 회원 아닐 시"),
    고정매니저해제_매니저회원번호_회원아님("-2", "mypage.broadcast.manager.delete.number.error", "매니저 회원번호가 회원 아닐 시"),
    고정매니저해제_등록된매니저아님("-3", "mypage.broadcast.manager.delete.add.manager", "등록된 매니저가 아닐 시"),
    고정매니저해제_실패("C006", "mypage.broadcast.manager.delete.fail", "고정매니저 해제 실패 시"),
    //마이페이지 방송설정 블랙리스트 조회
    블랙리스트조회_성공("C001", "mypage.broadcast.blacklist.select.success", "블랙리스트 조회 성공 시"),
    블랙리스트조회_없음("0", "mypage.broadcast.blacklist.select.no.success", "조회 내역 없을 시"),
    블랙리스트조회_요청회원번호_회원아님("-1", "mypage.broadcast.blacklist.select.member.number.error","요청회원번호가 회원 아닐 시"),
    블랙리스트조회_실패("C006", "mypage.broadcast.blacklist.select.fail", "블랙리스트 조회 실패 시"),
    //마이페이지 방송설정 블랙리스트 등록
    블랙리스트등록_성공("0", "mypage.broadcast.blacklist.add.success", "블랙리스트 등록 성공 시"),
    블랙리스트등록_요청회원번호_회원아님("-1", "mypage.broadcast.blacklist.add.member.number.error", "요청회원번호가 회원 아닐 시"),
    블랙리스트등록_블랙회원번호_회원아님("-2", "mypage.broadcast.blacklist.add.number.error", "블랙리스트 회원번호가 회원 아닐 시"),
    블랙리스트등록_이미블랙등록("-3", "mypage.broadcast.blacklist.add.already.add", "이미 블랙리스트 등록 되어있을 시"),
    블랙리스트등록_본인등록안됨("-4", "mypage.broadcast.blacklist.add.impossible.add", "본인을 블랙리스트로 등록하려고 할떄"),
    블랙리스트등록_실패("C006", "mypage.broadcast.blacklist.add.fail", "블랙리스트 등록 실패 시"),
    //마이페이지 방송설정 블랙리스트 해제
    블랙리스트해제_성공("0", "mypage.broadcast.blacklist.delete.success", "블랙리스트 해제 성공 시"),
    블랙리스트해제_요청회원번호_회원아님("-1", "mypage.broadcast.blacklist.delete.member.number.error", "요청회원번호가 회원 아닐 시"),
    블랙리스트해제_블랙회원번호_회원아님("-2", "mypage.broadcast.blacklist.delete.number.error", "블랙리스트 회원번호가 회원 아닐 시"),
    블랙리스트해제_블랙회원없음("-3", "mypage.broadcast.blacklist.delete.add.blacklist.no", "등록된 블랙리스트 회원이 없을 시"),
    블랙리스트해제_실패("C006", "mypage.broadcast.blacklist.delete.fail", "블랙리스트 해제 실패 시"),
    //별 달 교환아이템 조회
    별_달_교환아이템_조회_성공("C001", "mypage.change.item.select.success", "별 달 교환아이템 조회 성공 시"),
    별_달_교환아이템_조회_없음("0", "mypage.change.item.select.no.success", "별 달 교환아이템 없음"),
    별_달_교환아이템_조회_요청회원번호_회원아님("-1", "mypage.change.item.select.member.number.error", "요청회원번호 회원 아닐 시"),
    별_달_교환아이템_조회_IOS_지원안함("-2", "mypage.change.item.select.no.support.ios", "IOS 지원 안할 시"),
    별_달_교환아이템_조회_실패("C006", "mypage.change.item.select.fail", "별 달 교환아이템 조회 실패 시"),
    //별 달 교환하기
    별_달_교환하기_성공("0", "mypage.change.item.success", "별 달 교환하기 성공 시"),
    별_달_교환하기_요청회원번호_회원아님("-1", "mypage.change.item.member.number.error", "요청회원번호 회원 아닐 시"),
    별_달_교환하기_IOS_지원안함("-2", "mypage.change.item.no.support.ios", "IOS 지원 안할 시"),
    별_달_교환하기_상품코드없음("-3", "mypage.change.item.no.item.code", "상품코드 없을 시"),
    별_달_교환하기_별부족("-4", "mypage.change.item.byeol.lack", "별 부족 시"),
    별_달_교환하기_실패("C006", "mypage.change.item.fail", "별 달 교환하기 실패 시"),
    //별 달 자동교환 설정 변경
    별_달_자동교환설정변경_성공("0", "mypage.autoChange.setting.edit.success", "별 달 자동 교환 설정 변경 성공 시"),
    별_달_자동교환설정변경_요청회원번호_회원아님("-1", "mypage.autoChange.setting.edit.member.number.error", "요청회원번호 회원 아닐 시"),
    별_달_자동교환설정변경_실패("C006", "mypage.autoChange.setting.edit.fail", "별 달 자동 교환 설정 변경 실패 시"),
    //별 달 자동교환 설정 조회
    별_달_자동교환설정조회_성공("0", "mypage.autoChange.setting.select.success", "별 달 자동 교환 설정 조회 성공 시"),
    별_달_자동교환설정조회_요청회원번호_회원아님("-1", "mypage.autoChange.setting.select.member.number.error", "요청회원번호 회원 아닐 시"),
    별_달_자동교환설정조회_실패("C006", "mypage.autoChange.setting.select.fail", "별 달 자동 교환 설정 조회 실패 시"),
    //메시지 사용 클릭
    메시지클릭업데이트_성공("0", "mypage.member.msg.click.update.success", "메시지 클릭 업데이트 성공 시"),
    메시지클릭업데이트_오류("C006", "mypage.member.msg.click.update.error", "메시지 클릭 업데이트 오류 시"),
    //마이페이지 방송설정 조회하기
    방송설정조회_성공("0", "mypage.broadcast.setting.select.success", "방송 설정조회 성공 시"),
    방송설정조회_회원아님("-1", "mypage.broadcast.setting.member.number.error", "방송설정 회원아닐 시"),
    방송설정조회_실패("C006", "mypage.broadcast.setting.fail", "방송설정 오류 시"),
    //마이페이지 방송설정
    방송설정수정_성공("0", "mypage.broadcast.setting.edit.success", "방송설정 수정 성공 시"),
    방송설정수정_회원아님("-1", "mypage.broadcast.setting.edit.member.number.error", "방송설정 수정 회원아닐 시"),
    방송설정수정_실패("C006", "mypage.broadcast.setting.edit.error", "방송설정 수정 실패 시"),
    실시간팬배지_ON("0", "mypage.broadcast.setting.live.badge.on.success", "실시간 팬 배지 ON"),
    실시간팬배지_OFF("0", "mypage.broadcast.setting.live.badge.off.success", "실시간 팬 배지 OFF"),
    청취정보_ON("0", "mypage.broadcast.setting.listen.open.on.success", "청취정보공개 ON"),
    청취정보_HALFON("0", "mypage.broadcast.setting.listen.open.half.on.success", "청취정보공개 HALF ON"),
    청취정보_OFF("0", "mypage.broadcast.setting.listen.open.off.success", "청취정보공개 OFF"),
    선물스타추가_ON("0", "mypage.broadcast.setting.gift.star.on.success", "선물시자동스타추가 ON"),
    선물스타추가_OFF("0", "mypage.broadcast.setting.gift.star.off.success", "선물시자동스타추가 OFF"),
    메시지사용_ON("0", "mypage.broadcast.setting.mailbox.on.success", "메시지사용여부 ON"),
    메시지사용_OFF("0", "mypage.broadcast.setting.mailbox.off.success", "메시지사용여부 OFF"),
    // 방송 설정 추가 ( tts, sound 사용여부 )
    TTS_아이템_ON("0", "mypage.broadcast.setting.gift.tts.on.success", "TTS 아이템 사용여부 ON"),
    TTS_아이템_OFF("0", "mypage.broadcast.setting.gift.tts.off.success", "TTS 아이템 사용여부 OFF"),
    SOUND_아이템_ON("0", "mypage.broadcast.setting.gift.sound.on.success", "Sound 아이템 사용여부 ON"),
    SOUND_아이템_OFF("0", "mypage.broadcast.setting.gift.sound.off.success", "Sound 아이템 사용여부 OFF"),
    청취자_입장표시_ON("0", "mypage.broadcast.setting.listener.come.on.success", "청취자 입장표시 ON"),
    청취자_입장표시_OFF("0", "mypage.broadcast.setting.listener.come.off.success", "청취자 입장표시 OFF"),
    청취자_퇴장표시_ON("0", "mypage.broadcast.setting.listener.out.on.success", "청취자 퇴장표시 ON"),
    청취자_퇴장표시_OFF("0", "mypage.broadcast.setting.listener.out.off.success", "청취자 퇴장표시 OFF"),
    클립재생시간_10분초과("C005", "clip.exceed.play.time.limit.10min", "클립 재생시간 초과 시"),
    //내지갑 필터 팝업 조회
    내지갑팝업조회_성공("1", "mypage.wallet.history.popup.select.success", "내지갑 내역 조회 성공 시"),
    내지갑팝업조회_회원아님("-1", "mypage.wallet.history.popup.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    내지갑팝업조회_실패("C006", "mypage.wallet.history.popup.select.fail", "내지갑 내역 조회 실패 시"),
    //방송방 사연 조회
    방송방사연_조회_성공("C001", "mypage.broad.story.list.select.success", "방송방 사연 조회 성공 시"),
    방송방사연_조회_없음("0", "mypage.broad.story.list.select.no.success", "방송방 사연 없을 시"),
    방송방사연_조회_회원아님("-1", "mypage.broad.story.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    방송방사연_조회_실패("C006", "mypage.broad.story.list.select.fail", "방송방 사연 조회 실패 시"),
    //내지갑 내역 조회
    내지갑_내역조회_성공("C001", "mypage.wallet.list.select.success", "내지갑 내역 조회 성공 시"),
    내지갑_내역조회_없음("0", "mypage.wallet.list.select.no", "내지갑 내역 없을 시"),
    내지갑_내역조회_회원아님("-1", "mypage.wallet.list.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    내지갑_내역조회_실패("C006", "mypage.wallet.list.select.fail", "내지갑 내역조회 실패 시"),
    //내클립 현황
    내클립조회_성공("0", "my.clip.select.success", "내 클립 조회 성공 시"),
    내클립조회_요청회원번호_정상아님("-1", "my.clip.select.member.numer.error", "요청회원번호가 회원 아닐 시"),
    내클립조회_실패("C006", "my.clip.select.fail", "내 클립 조회 실패 시"),
    //내 클립 현황 상세 조회
    내클립현황상세조회_성공("C001", "my.clip.detail.select.success", "내 클립 상세 조회 성공 시"),
    내클립현황상세조회_실패("C006", "my.clip.detail.select.fail", "내 클립 상세 조회 실패 시"),
    //내 클립 현황 상세 조회 시 클립 공개/비공개 설정
    내클립현황_클립공개여부수정_성공("0", "my.clip.detail.edit.success", "내 클립 현황 공개여부 수정 성공 시"),
    내클립현황_클립공개여부수정_회원아님("-1", "my.clip.detail.edit.member.number.error", "내 클립 현황 공개여부 수정 성공 시"),
    내클립현황_클립공개여부수정_클립없음("-2", "my.clip.detail.edit.no.clip", "내 클립 현황 공개여부 수정 성공 시"),
    내클립현황_클립공개여부수정_수정권한없음("-3", "my.clip.detail.edit.no.auth", "내 클립 현황 공개여부 수정 성공 시"),
    내클립현황_클립공개여부수정_실패("C006", "my.clip.detail.edit.fail", "내 클립 현황 공개여부 수정 실패 시"),
    //마이페이지 공지사항 댓글
    공지댓글등록_성공("0", "mypage.notice.reply.add.success", "마이페이지 공지 댓글 등록 성공 시"),
    공지댓글등록_공지회원번호없음("-1", "mypage.notice.reply.add.object.member.number.error", "대상 회원번호가 회원이 아닐 시"),
    공지댓글등록_작성자회원번호없음("-2", "mypage.notice.reply.add.writer.member.number.error", "요청 회원번호가 회원이 아닐 시"),
    공지댓글등록_공지번호없음("-3", "mypage.notice.reply.add.no.noticeIdx", "등록할 공지 번호가 없을 시"),
    공지댓글등록_댓글내용없음("-4", "mypage.notice.reply.add.no.contents", "등록할 공지 댓글 내용이 없을 시"),
    공지댓글등록_실패("C006", "mypage.notice.reply.add.fail", "마이페이지 공지 댓글 등록 실패 시"),
    공지댓글삭제_성공("0", "mypage.notice.reply.delete.success", "마이페이지 공지 댓글 삭제 성공 시"),
    공지댓글삭제_공지회원번호없음("-1", "mypage.notice.reply.delete.object.member.number.error", "대상 회원번호가 회원이 아닐 시"),
    공지댓글삭제_삭제자회원번호없음("-2", "mypage.notice.reply.delete.writer.member.number.error", "요청 회원번호가 회원이 아닐 시"),
    공지댓글삭제_댓글번호없음("-3", "mypage.notice.reply.delete.no.replyIdx", "삭제할 댓글 번호가 없을 시"),
    공지댓글삭제_댓글번호등록공지불일치("-4", "mypage.notice.reply.delete.star.notice.mismatch", "댓글의 회원번호와 해당 공지의 회원번호가 불일치 시"),
    공지댓글삭제_이미삭제됨("-5", "mypage.notice.reply.delete.already.delete", "이미 삭제된 댓글일 시"),
    공지댓글삭제_삭제권한없음("-6", "mypage.notice.reply.delete.no.auth", "댓글 삭제 권한이 없을 시"),
    공지댓글삭제_실패("C006", "mypage.notice.reply.delete.fail", "마이페이지 공지 댓글 삭제 실패 시"),
    공지댓글수정_성공("0", "mypage.notice.reply.edit.success", "마이페이지 공지 댓글 수정 성공 시"),
    공지댓글수정_공지회원번호없음("-1", "mypage.notice.reply.edit.object.member.number.error", "대상 회원번호가 회원이 아닐 시"),
    공지댓글수정_수정자회원번호없음("-2", "mypage.notice.reply.edit.writer.member.number.error", "수정자 회원번호가 회원이 아닐 시"),
    공지댓글수정_댓글번호없음("-3", "mypage.notice.reply.edit.no.replyIdx", "수정할 댓글 번호가 없을 시"),
    공지댓글수정_댓글번호등록공지불일치("-4", "mypage.notice.reply.edit.star.notice.mismatch", "댓글의 회원번호와 해당 공지의 회원번호가 불일치 시"),
    공지댓글수정_삭제된댓글("-5", "mypage.notice.reply.edit.already.delete", "삭제된 댓글일 시"),
    공지댓글수정_수정권한없음("-6", "mypage.notice.reply.edit.no.auth", "댓글 수정 권한이 없을 시"),
    공지댓글수정_실패("C006", "mypage.notice.reply.edit.fail", "마이페이지 공지 댓글 수정 실패 시"),
    공지댓글보기_성공("C001","mypage.notice.reply.select.success","마이페이지 공지 댓글 보기 성공 시"),
    공지댓글보기_댓글없음("0","mypage.notice.reply.select.no.reply.success","댓글이 없을 시"),
    공지댓글보기_회원번호없음("-1", "mypage.notice.reply.select.member.number.error", "요청회원번호 회원 아닐 시"),
    공지댓글보기_공지회원번호없음("-2","mypage.notice.reply.select.object.member.number.error","대상 회원번호가 회원이 아닐 시"),
    공지댓글보기_실패("C006", "mypage.notice.reply.select.fail", "마이페이지 공지 댓글 보기 실패 시"),
    //추천DJ목록조회
    추천DJ목록조회_성공("C001", "dj.recommend.list.select.success", "추천DJ 목록 조회 성공 시"),
    추천DJ목록조회_실패("C006", "dj.recommend.list.select.fail", "추천DJ 목록 조회 실패 시"),
    //프레임 조회
    프레임조회_성공("C001", "mypage.frame.select.success", "프레임 조회 성공 시"),
    프레임조회_실패("C006", "mypage.frame.select.fail", "프레임 조회 실패 시"),
    //프레임 편집
    편집_성공("0", "mypage.frame.edit.success", "프레임 편집 성공 시"),
    편집_회원아님("-1", "mypage.frame.edit.member.number.error", "요청회원번호 회원 아닐 시"),
    편집_실패("C006", "mypage.frame.edit.error", "프레임 편집 실패 시"),
    //프로필이미지 추가등록
    프로필이미지_추가등록_성공("0", "profile.img.add.success", "프로필 이미지 추가등록 시"),
    프로필이미지_추가등록_회원아님("-1", "profile.img.add.member.number.error", "요청회원번호 회원 아닐 시"),
    프로필이미지_추가등록_이미지없음("-2", "profile.img.add.no.img", "이미지 없을 시"),
    프로필이미지_추가등록_10개초과("-3", "profile.img.add.limit.10", "이미지 10개 초과 시"),
    프로필이미지_추가등록_실패("C006", "profile.img.add.fail", "프로필 이미지 추가등록 실패 시"),
    //프로필이미지 삭제
    프로필이미지_삭제_성공("0", "profile.img.delete.success", "프로필 이미지 삭제 시"),
    프로필이미지_삭제_회원아님("-1", "profile.img.delete.member.number.error", "요청회원번호 회원 아닐 시"),
    프로필이미지_삭제_이미지없음("-2", "profile.img.delete.no.img", "이미지 없을 시"),
    프로필이미지_삭제_실패("C006", "profile.img.delete.fail", "프로필 이미지 삭제 실패 시"),
    //프로필이미지 대표지정
    프로필이미지_대표지정_성공("0", "profile.img.leader.success", "프로필 이미지 대표지정 시"),
    프로필이미지_대표지정_회원아님("-1", "profile.img.leader.member.number.error", "요청회원번호 회원 아닐 시"),
    프로필이미지_대표지정_실패("C006", "profile.img.leader.fail", "프로필 이미지 대표지정 실패 시"),
    //영상대화 방송설정 블랙리스트 등록
    영상대화_차단등록_성공("0", "mypage.video.blacklist.add.success", "블랙리스트 등록 성공 시"),
    영상대화_차단등록_요청회원번호_회원아님("-1", "mypage.video.blacklist.add.member.number.error", "요청회원번호가 회원 아닐 시"),
    영상대화_차단등록_대상회원번호_회원아님("-2", "mypage.video.blacklist.add.number.error", "블랙리스트 회원번호가 회원 아닐 시"),
    영상대화_차단등록_이미블랙등록("-3", "mypage.video.blacklist.add.already.add", "이미 블랙리스트 등록 되어있을 시"),
    영상대화_차단등록_본인등록안됨("-4", "mypage.video.blacklist.add.impossible.add", "본인을 블랙리스트로 등록하려고 할떄"),
    영상대화_차단등록_회원차단상태("-5", "mypage.video.blacklist.add.member.black.already", "이미 전체 회원차단 시"),
    영상대화_차단등록_실패("C006", "mypage.video.blacklist.add.fail", "블랙리스트 등록 실패 시"),
    //영상대화 수신설정 조회하기
    영상대화_설정조회_성공("0", "mypage.member.video.select.success", "영상대화 설정 조회 성공 시"),
    영상대화_설정조회_회원아님("-1", "mypage.member.video.select.member.number.error", "회원아닐 시"),
    영상대화_설정조회오류("C006", "mypage.member.video.select.error", "영상대화 설정 조회 오류 시"),
    //영상대화 수신설정 수정
    영상대화_수신설정수정_성공("0", "mypage.video.setting.edit.success", "영상대화 수신설정 수정 성공 시"),
    영상대화_알림무음("0", "mypage.member.video.edit.all.silent.success", "알림 모드 무음"),
    영상대화_알림소리("0", "mypage.member.video.edit.all.sound.success", "알림 모드 소리"),
    영상대화_알림진동("0", "mypage.member.video.edit.all.vibration.success", "알림 모드 진동"),
    영상대화_수신설정수정_회원아님("-1", "mypage.member.video.edit.memer.number.error", "요청회원번호가 회원 아닐 시"),
    영상대화_수신설정수정_오류("C006", "mypage.video.setting.edit.fail", "영상대화 수신설정 수정 실패 시"),
    //사연 보관함 조회
    사연보관함_조회_성공("0", "mypage.story.select.success", "사연 보관함 내역 조회 성공 시"),
    사연보관함_조회_실패("C006","mypage.story.select.fail", "사연 보관함 내역 조회 실패 시");


    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    MypageStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
