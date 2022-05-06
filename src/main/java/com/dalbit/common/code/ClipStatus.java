package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum ClipStatus implements Status {


    //클립수정
    클립수정_클립번호없음("-1", "clip.play.number.error", "클립번호 없음"),
    클립수정_수정변화없음("-2", "clip.no.change", "수정할 내용이 없음"),
    클립수정_잘못된구분값("-3", "clip.no.match.editSlct", "editSlct 값이 잘못됨"),
    클립수정_이미삭제("-4", "clip.already.delete", "이미 삭제됨"),
    클립수정_이미확인("-5", "clip.already.confirm", "이미 확인됨"),
    클립수정_닉네임중복("-6", "member.join.nick.duplicate", "닉네임중복됨"),
    //클립 등록
    클립등록_성공("0", "clip.add.success", "클립 등록 성공 시"),
    클립등록_회원아님("-1", "clip.add.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립등록_20세제한("-2", "clip.add.member.limit.20age", "20세 미만인 회원일 경우"),
    클립등록_실패("C006", "clip.add.fail", "클립 등록 실패 시"),
    //클립 리스트 조회
    클립리스트_조회_성공("C001", "clip.list.select.success", "클립 리스트 조회 성공 시"),
    클립리스트_조회_없음("0", "clip.list.select.no.success", "클립 리스트 없음"),
    클립리스트_조회_실패("C006", "clip.list.select.fail", "클립 리스트 조회 실패 시"),
    //클립 플레이
    클립플레이_성공("0", "clip.play.success", "클립 플레이 성공 시"),
    클립플레이_회원아님("-1", "clip.play.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립플레이_클립번호없음("-2", "clip.play.number.error", "클립 번호가 없을 시"),
    클립플레이_팬아님("-3", "clip.play.no.fan", "팬이 아닐 시"),
    클립플레이_20세미만("-4", "clip.play.limit.20age", "20세 미만일 시"),
    클립플레이_차단회원재생불가("-5", "clip.play.black.list.play.no", "차단회원의 클립 재생 불가 시"),
    클립플레이_실패("C006", "clip.play.fail", "클립 플레이 실패 시"),
    //클립 재생목록 조회
    클립_재생목록_조회_성공("C001", "clip.play.list.select.success", "클립 플레이리스트 조회 성공 시"),
    클립_재생목록_조회_없음("0", "clip.play.list.select.no.success", "클립 플레이리스트 없음"),
    클립_재생목록_조회_실패("C006", "clip.play.list.select.fail", "클립 플레이리스트 조회 실패 시"),
    //클립 선물하기
    클립_선물하기_성공("0", "clip.gift.success", "선물하기 성공 시"),
    클립_선물하기_요청회원_회원아님("-1", "clip.gift.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_선물하기_대상회원_회원아님("-2", "clip.gift.object.member.number.error", "대상회원번호가 회원 아닐 시"),
    클립_선물하기_클립번호없음("-3", "clip.gift.no.clip.number", "클립 번호가 없을 시"),
    클립_선물하기_클립번호_대상회원번호_불일치("-4", "clip.gift.clip.number.object.member.number.unmatch", "클립 번호와 대상회원번호 불일치 시"),
    클립_선물하기_본인불가("-5", "clip.gift.impossible.me", "선물하기 본인불가 시"),
    클립_선물하기_아이템코드없음("-6", "clip.gift.no.item.code", "아이템코드 없을 시"),
    클립_선물하기_달부족("-7", "clip.gift.dal.limit", "달 부족 시"),
    클립_선물하기_실패("C006", "clip.gift.fail", "클립 선물하기 실패 시"),
    //클립 좋아요
    클립_좋아요_성공("0", "clip.good.success", "좋아요 성공 시"),
    클립_좋아요_해제_성공("0", "clip.good.cancel.success", "좋아요 해제 시"),
    클립_좋아요_요청회원_회원아님("-1", "clip.good.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_좋아요_클립번호없음("-2", "clip.good.no.clip.number", "클립 번호가 없을 시"),
    클립_좋아요_이미좋아요누름("-3", "clip.good.already.good", "이미 좋아요 눌렀을 시"),
    클립_좋아요_변화없음("-4", "clip.good.no.change", "좋아요 변화없을 시"),
    클립_좋아요_차단회원("-5", "clip.good.black.list", "차단회원일 경우"),
    클립_좋아요_실패("C006", "clip.good.fail", "좋아요 실패 시"),
    //클립 선물랭킹 TOP3
    클립_선물랭킹_TOP3_조회_성공("C001", "clip.gift.rank.top3.select.success", "클립 선물랭킹 TOP3 조회 시"),
    클립_선물랭킹_TOP3_조회_없음("0", "clip.gift.rank.top3.select.no.success", "클립 선물랭킹 TOP3 없음"),
    클립_선물랭킹_TOP3_조회_실패("C006", "clip.gift.rank.top3.select.fail", "클립 선물랭킹 TOP3 조회 실패 시"),
    //클립 선물랭킹
    클립_선물랭킹_조회_성공("C001", "clip.gift.rank.select.success", "클립 선물랭킹 조회 성공 시"),
    클립_선물랭킹_조회_없음("0", "clip.gift.rank.select.no.success", "클립 선물랭킹 없음"),
    클립_선물랭킹_조회_실패("C006", "clip.gift.rank.select.fail", "클립 선물랭킹 조회 실패 시"),
    //클립 받은선물 내역
    클립_받은선물내역_조회_성공("C001", "clip.received.gift.list.select.success", "클립 받은선물내역 조회 성공 시"),
    클립_받은선물내역_조회_없음("0", "clip.received.gift.list.select.no.success", "클립 받은선물내역 없을 시"),
    클립_받은선물내역_조회_요청회원_회원아님("-1", "clip.received.gift.list.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_받은선물내역_조회_클립없음("-2", "clip.received.gift.list.select.no.clip", "클립가 없을 시"),
    클립_받은선물내역_조회_요청회원번호_클립회원번호아님("-3", "clip.gift.list.select.member.number.clip.member.number.unmatch", "요청회원번호가 클립 회원번호가 아닐 시"),
    클립_받은선물내역_조회_실패("C006", "clip.received.gift.list.select.fail", "클립 받은선물내역 조회 실패 시"),
    //클립 수정
    클립수정_성공("0", "clip.edit.success", "클립 수정 성공 시"),
    클립수정_회원아님("-1", "clip.edit.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립수정_클립없음("-2", "clip.edit.no.clip", "클립 없을 시"),
    클립수정_회원번호_클립회원번호아님("-3", "clip.edit.member.number.clip.member.number.unmatch", "요청회원번호가 클립 회원번호가 아닐 시"),
    클립수정_실패("C006", "clip.edit.fail", "클립 등록 실패 시"),
    //클립 삭제
    클립삭제_성공("0", "clip.delete.success", "클립 삭제 성공 시"),
    클립삭제_회원아님("-1", "clip.delete.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립삭제_클립없음("-2", "clip.delete.no.clip", "클립 없을 시"),
    클립삭제_회원번호_클립회원번호아님("-3", "clip.delete.member.number.clip.member.number.unmatch", "요청회원번호가 클립 회원번호가 아닐 시"),
    클립삭제_실패("C006", "clip.delete.fail", "클립 삭제 실패 시"),
    //클립 댓글목록 조회
    클립_댓글목록_조회_성공("C001", "clip.reply.list.select.success", "클립 댓글목록 조회 성공 시"),
    클립_댓글목록_조회_없음("0", "clip.reply.list.select.no.success", "클립 댓글목록 없을 시"),
    클립_댓글목록_조회_회원아님("-1", "clip.reply.list.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_댓글목록_조회_클립없음("-2", "clip.reply.list.select.no.clip", "클립 없을 시"),
    클립_댓글목록_조회_실패("C006", "clip.reply.list.select.fail", "클립 댓글목록 조회 실패 시"),
    //클립 댓글등록
    클립_댓글등록_성공("0", "clip.reply.add.success", "클립 댓글등록 성공 시"),
    클립_댓글등록_회원아님("-1", "clip.reply.add.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_댓글등록_클립없음("-2", "clip.reply.add.no.clip", "클립 없을 시"),
    클립_댓글등록_차단회원("-3", "clip.reply.add.black.list", "차단회원일 경우"),
    클립_댓글등록_실패("C006", "clip.reply.add.fail", "클립 댓글등록 실패 시"),
    //클립 댓글수정
    클립_댓글수정_성공("0", "clip.reply.edit.success", "클립 댓글수정 성공 시"),
    클립_댓글수정_회원아님("-1", "clip.reply.edit.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_댓글수정_클립없음("-2", "clip.reply.edit.no.clip", "클립 없을 시"),
    클립_댓글수정_댓글번호없음("-3", "clip.reply.edit.no.reply.idx", "댓글번호 없을 시"),
    클립_댓글수정_작성자아님("-4", "clip.reply.edit.not.writer", "댓글 작성자가 아닐 시"),
    클립_댓글수정_실패("C006", "clip.reply.edit.fail", "클립 댓글수정 실패 시"),
    //클립 댓글삭제
    클립_댓글삭제_성공("0", "clip.reply.delete.success", "클립 댓글삭제 성공 시"),
    클립_댓글삭제_회원아님("-1", "clip.reply.delete.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_댓글삭제_클립없음("-2", "clip.reply.delete.no.clip", "클립 없을 시"),
    클립_댓글삭제_댓글번호없음("-3", "clip.reply.delete.no.reply.idx", "댓글번호 없을 시"),
    클립_댓글삭제_작성자아님("-4", "clip.reply.delete.not.writer", "댓글 작성자가 아닐 시"),
    클립_댓글삭제_실패("C006", "clip.reply.delete.fail", "클립 댓글삭제 실패 시"),
    //클립 업로드 목록 조회
    클립_업로드목록_조회_성공("C001", "clip.upload.list.select.success", "클립 업로드 목록 조회 성공 시"),
    클립_업로드목록_조회_없음("0", "clip.upload.list.select.no.success", "클립 업로드 목록 없을 시"),
    클립_업로드목록_조회_회원아님("-1", "clip.upload.list.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_업로드목록_조회_스타회원아님("-2", "clip.upload.list.select.star.member.number.error", "스타회원번호가 회원 아닐 시"),
    클립_업로드목록_조회_실패("C006", "clip.upload.list.select.fail", "클립 업로드 목록 조회 실패 시"),
    //클립 청취내역 조회
    클립_청취내역_조회_성공("C001", "clip.listen.list.select.success", "클립 청취내역 목록 조회 성공 시"),
    클립_청취내역_조회_없음("0", "clip.listen.list.select.no.success", "클립 청취내역 목록 없을 시"),
    클립_청취내역_조회_회원아님("-1", "clip.listen.list.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_청취내역_조회_스타회원아님("-2", "clip.listen.list.select.star.member.number.error", "스타회원번호가 회원 아닐 시"),
    클립_청취내역_조회_실패("C006", "clip.listen.list.select.fail", "클립 청취내역 목록 조회 실패 시"),
    //클립 재생목록 편집
    클립_재생목록편집_성공("0", "clip.play.list.edit.success", "클립 재생목록 편집 성공 시"),
    클립_재생목록편집_회원아님("-1", "clip.play.list.edit.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_재생목록편집_실패("C006", "clip.play.list.edit.fail", "클립 재생목록 편집 실패 시"),
    //클립 메인 인기리스트 조회
    클립_메인_인기리스트_조회_성공("C001", "clip.main.pop.list.select.success", "클립 메인 인기목록 조회 성공 시"),
    클립_메인_인기리스트_조회_없음("0", "clip.main.pop.list.select.no.success", "클립 메인 인기목록 없을 시"),
    클립_메인_인기리스트_조회_회원아님("-1", "clip.main.pop.list.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_메인_인기리스트_조회_실패("C006", "clip.main.pop.list.select.fail", "클립 메인 인기목록 조회 실패 시"),
    //클립 메인 최신리스트 조회
    클립_메인_최신리스트_조회_성공("C001", "clip.main.latest.list.select.success", "클립 메인 최신목록 조회 성공 시"),
    클립_메인_최신리스트_조회_없음("0", "clip.main.latest.list.select.no.success", "클립 메인 최신목록 없을 시"),
    클립_메인_최신리스트_조회_회원아님("-1", "clip.main.latest.list.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_메인_최신리스트_조회_실패("C006", "clip.main.latest.list.select.fail", "클립 메인 최신목록 조회 실패 시"),
    //클립 메인 주제별TOP3 조회
    클립_메인_주제별TOP3_조회_성공("C001", "clip.main.subject.top3.list.select.success", "클립 메인 주제별 TOP3 조회 성공 시"),
    클립_메인_주제별TOP3_조회_없음("0", "clip.main.subject.top3.list.select.no.success", "클립 메인 주제별 TOP3 없을 시"),
    클립_메인_주제별TOP3_조회_회원아님("-1", "clip.main.subject.top3.list.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    클립_메인_주제별TOP3_조회_실패("C006", "clip.main.subject.top3.list.select.fail", "클립 메인 주제별 TOP3 조회 실패 시"),
    //클립 신고하기
    클립신고_성공("0", "clip.declar.success", "클립 신고 성공 시"),
    클립신고_요청회원번호_정상아님("-1", "clip.declar.member.number.error", "요청 회원번호 정상 아닐 시"),
    클립신고_신고회원번호_정상아님("-2", "clip.declar.clip.member.number.error", "클립 회원번호 정상 아닐 시"),
    클립신고_이미_신고상태("-3", "clip.declar.already", "이미 신고 상태 시"),
    클립신고_실패("C006", "clip.declar.fail", "클립 신고 신청 실패 시"),
    //클립 공유하기 주소
    클립공유_성공("0", "clip.share.success", "클립 공유 성공 시"),
    클립공유_데이터없음("-1", "clip.share.data.no.error", "클립 조회없을 때"),
    클립공유_동적링크실패("-2", "clip.share.link.error", "클립 동적링크 생성 실패"),
    //추천 방송 검색
    추천방송검색_성공("C001", "recommand.broadcast.search.success", "추천 방송 검색 성공 시"),
    추천방송검색_결과없음("0", "recommand.broadcast.search.no.success", "결과 없을 시"),
    추천방송검색_실패("C006", "recommand.broadcast.search.fail", "추천 방송 검색 실패 시"),
    //클립 재생 확인
    재생확인_성공("0", "clip.play.confirm.success", "재생확인 성공 시"),
    재생확인_요청회원번호_정상아님("-1", "clip.play.confirm.member.number.error", "요청회원번호가 회원 아닐 시"),
    재생확인_클립번호없음("-2", "clip.play.confirm.clip.number.no", "클립번호 없을 시"),
    재생확인_번호없음("-3", "clip.play.confirm.play.idx.no", "재생확인번호 없을 시"),
    재생확인_클립번호_재생확인번호_불일치("-4", "clip.play.confirm.number.unmatch", "클립번호와 재생확인번호 불일치 시"),
    재생확인_이미확인("-5", "clip.play.confirm.already", "이미 확인 시"),
    재생확인_60초미만("-6", "clip.play.confirm.60sec.limit", "60초 미만일 시"),
    재생확인_실패("C006", "clip.play.confirm.fail", "재생확인 실패 시"),
    //클립랭킹
    클립랭킹조회_성공("C001", "clip.rank.select.success", "클립랭킹 조회 성공 시"),
    클립랭킹조회_실패("C006", "clip.rank.select.fail", "클립랭킹 조회 실패 시"),
    //달대리 추천클립
    달대리추천클립조회_성공("C001", "clip.recommend.list.select.success", "달대리 추천 클립 조회 성공 시"),
    달대리추천클립조회_회원아님("-1", "clip.recommend.list.member.number.error", "요청회원번호 회원 아닐 시"),
    달대리추천클립조회_실패("C006", "clip.recommend.list.select.fail", "달대리 추천 클립 조회 실패 시"),
    //클립랭킹 일간보상
    클립일간보상_성공("C001", "clip.reward.day.success", "클립 일간보상 조회 성공 시"),
    클립일간보상_회원아님("-1", "clip.reward.day.member.number.error", "요청회원번호 회원 아닐 시"),
    클립일간보상_탑3아님("-2", "clip.reward.day.top3.no", "탑3 아닐 시"),
    클립일간보상_없음("-3", "clip.reward.day.no", "일간보상이 없을 시"),
    클립일간보상_실패("C006", "clip.reward.day.fail", "클립 일간보상 조회 실패 시"),
    //클립랭킹 주간보상
    클립주간보상_성공("C001", "clip.reward.week.success", "클립 주간보상 조회 성공 시"),
    클립주간보상_회원아님("-1", "clip.reward.week.member.number.error", "요청회원번호 회원 아닐 시"),
    클립주간보상_탑3아님("-2", "clip.reward.week.top3.no", "탑3 아닐 시"),
    클립주간보상_없음("-3", "clip.reward.week.no", "주간보상이 없을 시"),
    클립주간보상_실패("C006", "clip.reward.week.fail", "클립 주간보상 조회 실패 시"),
    //클립 소감작성
    소감작성_성공("0", "clip.win.msg.success", "소감작성 성공 시"),
    소감작성_회원아님("-1", "clip.win.member.number.error", "요청회원번호 회원 아닐 시"),
    소감작성_탑3아님("-2", "clip.win.top3.no", "탑3 아닐 시"),
    소감작성_실패("C006", "clip.win.msg.fail", "소감작성 실패 시"),
    //플레이리스트 폴더 조회
    폴더조회_성공("C001", "folder.list.select.success", "폴더 조회 성공 시"),
    폴더조회_실패("C006", "folder.list.select.fail", "폴더 조회 실패 시"),
    //플레이리스트 폴더 추가
    폴더추가_성공("0", "folder.add.success", "폴더 추가 성공 시"),
    폴더추가_회원아님("-1", "folder.add.member.number.error", "요청회원번호 회원 아닐 시"),
    폴더추가_10개초과("-2", "folder.add.10.limit", "폴더 10개 초과 시"),
    폴더추가_실패("C006", "folder.add.fail", "폴더 추가 실패 시"),
    //플레이리스트 폴더 삭제
    폴더삭제_성공("0", "folder.delete.success", "폴더 삭제 성공 시"),
    폴더삭제_회원아님("-1", "folder.delete.member.number.error", "요청회원번호 회원 아닐 시"),
    폴더삭제_없음("-2", "folder.delete.no", "폴더가 없을 경우"),
    폴더삭제_실패("C006", "folder.delete.fail", "폴더 삭제 실패 시"),
    //플레이리스트 폴더 수정
    폴더수정_성공("0", "folder.edit.success", "폴더 수정 성공 시"),
    폴더수정_회원아님("-1", "folder.edit.member.number.error", "요청회원번호 회원 아닐 시"),
    폴더수정_본인아님("-2", "folder.edit.not.me", "본인 폴더가 아닐 시"),
    폴더수정_실패("C006", "folder.edit.fail", "폴더 수정 실패 시"),
    //마이 플레이리스트 조회
    플레이리스트_조회_성공("C001", "my.playlist.select.success", "플레이리스트 조회 시"),
    플레이리스트_조회_실패("C006", "my.playlist.select.fail", "플레이리스트 조회 실패 시"),
    //마이 플레이리스트 추가
    플레이리스트_추가_성공("0", "my.playlist.add.success", "플레이리스트 추가 시"),
    플레이리스트_추가_회원아님("-1", "my.playlist.add.member.number.error", "요청회원번호 회원 아닐 시"),
    플레이리스트_추가_클립없음("-2", "my.playlist.add.clip.no", "클립이 없을 시"),
    플레이리스트_추가_100개초과("-3", "my.playlist.add.100.limit", "100개 초과 시"),
    플레이리스트_추가_중복("-4", "my.playlist.add.duplicate", "폴더 내 동일한 클립 존재 시"),
    플레이리스트_추가_폴더없음("-5", "my.playlist.add.not.folder", "폴더가 없을 시"),
    플레이리스트_추가_실패("C006", "my.playlist.add.fail", "플레이리스트 추가 실패 시"),
    //마이 플레이리스트 편집
    플레이리스트_수정_성공("0", "my.playlist.edit.success", "플레이리스트 수정 시"),
    플레이리스트_수정_회원아님("-1", "my.playlist.edit.member.number.error", "요청회원번호 회원 아닐 시"),
    플레이리스트_수정_실패("C006", "my.playlist.edit.fail", "플레이리스트 수정 실패 시");
    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    ClipStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
