package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum BroadcastStatus implements Status {


    //방송생성
    방송생성("0", "broadcast.room.start.success", "방송 생성 시"),
    방송생성_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송중인방존재("-2", "broadcast.room.existence", "방송중인 방이 있을 시"),
    방송생성_deviceUuid비정상("-3", "broadcast.room.deviceUuid.error", "deviceUuid 비정상일 시"),
    방송생성_20세제한("-4", "broadcast.room.limit.20age", "20세 미만 회원일 경우"),
    방송생성_3레벨제한("-5", "broadcast.room.limit.3level", "3레벨 제한"),
    방송생성_20세본인인증("-6", "broadcast.room.no.certification", "20세이상 방생성 시 본인인증 안한경우"),
    방송생성_청취중_방송생성("-7", "broadcast.room.already.listener", "청취중일경우 방송시작시 에러코드"),
    방생성실패("C006", "broadcast.room.fail", "방송 생성 실패 시"),
    //방송방 참가를 위한 스트림아이디, 토큰받아오기
    방송참여토큰발급("0", "broadcast.token.success", "토큰 발급(방참가) 성공 시"),
    방송참여토큰_해당방이없음("-1", "broadcast.token.room.no", "해당 방이 없을 시"),
    방송참여토큰_방장이없음("-2", "broadcast.token.bj.no", "BJ가 없을 시"),
    방송참여토큰발급_실패("C006", "broadcast.token.fail", "토큰 발급 실패 시"),
    //방송참여
    방송참여성공("0", "broadcast.room.join.success", "방송 참가 성공 시"),
    방송참여_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송참여_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송참여_종료된방송("-3", "broadcast.room.end", "종료된 방송일 시"),
    방송참여_이미참가("-4", "broadcast.room.join.already", "이미 참가 되어있을 시"),
    방송참여_입장제한("-5", "broadcast.room.join.no", "입장제한 시"),
    방송참여_나이제한("-6", "broadcast.room.join.no.age", "나이제한 시"),
    방송참여_강퇴시간제한("-7", "broadcast.room.join.kickout.timelimit", "강제퇴장후 24시간이 지나지 않았을 시"),
    방송참여_방송중("-8", "broadcast.room.join.already.live.dj", "방송중인 본인방 참가할 시"),
    방송참여_다른기기("-10", "broadcast.room.join.another", "다른기기에서 청취 시"),
    방송참여_블랙리스트("-11", "broadcast.room.join.blacklist", "블랙리스트에 등록되어 있을 시"),
    방송참여_비회원IP중복("-12", "broadcast.room.join.ano.ip.duplicate", "비회원 IP 동일방 중복 있을 시"),
    방송참여_차단회원입장불가("-13", "broadcast.room.black.list.join.no", "차단회원 입장 불가 시"),
    방송참여_20세본인인증안함("-14", "broadcast.room.join.no.certification", "20세이상 방 입장시 본인인증 안한 경우"),
    방송참여_방송중_다른방입장("-15", "broadcast.room.join.already.host", "방송중 청취시 에러코드"),
    방참가실패("C006", "broadcast.room.join.fail", "방송 참가 실패 시"),
    비회원_재진입("-99", "broadcast.room.join.anonymous.twice", "비회원이 방송방에 2번 이상 들어왔을 시"),
    방송방조인따라가기비공개("-999", "broadcast.room.join.not.allow.fallow", "일반회원 참여시 따라가기 비공개일 경우"),
    //방송나가기
    방송나가기("0", "broadcast.room.out.success", "방송 나가기 시"),
    방송나가기_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송나가기_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송나가기_종료된방송("-3", "broadcast.room.end", "종료된 방송일 시"),
    방송나가기_방참가자아님("-4", "broadcast.room.join.member.no", "방송 참가자 아닐 시"),
    방송나가기_DB_LOCK("-9", "broadcast.room.join.deadlock", "DB DEAD LOCK"),
    방송나가기실패("C006", "broadcast.out.fail", "방송 나가기 실패 시"),
    //방송정보수정
    방송정보수정성공("0", "broadcast.room.edit.success", "방송정보 수정 성공 시"),
    방송정보수정_회원아님("-1", "broadcast.room.edit.member.no", "회원이 아닐 시"),
    방송정보수정_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송정보수정_해당방에없는회원번호("-3", "broadcast.room.member.number.no", "해당 방에 없는 회원번호일 시"),
    방송정보수정_수정권한없는회원("-4", "broadcast.room.member.edit.no", "수정권한이 없는 회원일 시"),
    방송정보수정실패("C006", "broadcast.room.edit.fail", "방송정보 수정 실패 시"),
    //방송리스트
    방송리스트조회("C001", "broadcast.room.list.select.success", "방송리스트 정보 조회 성공 시"),
    방송리스트없음("0", "broadcast.room.list.no.success", "방송리스트가 없을 시"),
    방송리스트_회원아님("-1", "broadcast.room.list.member.number.no", "회원번호가 아닐 시"),
    방송리스트조회_실패("C006", "broadcast.room.list.select.fail", "방송리스트 정보 조회 실패 시"),
    //방송참여자 리스트
    방송참여자리스트_조회("C001", "broadcast.room.member.list.select.success", "방송참여자리스트 정보 조회 시"),
    방송참여자리스트없음("0", "broadcast.room.member.list.no.success", "방송참여자 리스트가 없을 시"),
    방송참여자리스트_회원아님("-1", "broadcast.room.member.list.number.no", "회원번호가 아닐 시"),
    방송참여자리스트_방없음("-2", "broadcast.room.member.list.room.no", "해당 방이 없을 시"),
    방송참여자리스트_참여자아님("-3", "broadcast.room.member.list.room.join.no", "해당 방이 참여자가 아닐 시"),
    방송참여자리스트조회_실패("C006", "broadcast.room.member.list.select.fail", "방송참여자 리스트 조회 실패 시"),
    //방송방 정보 보기
    방정보보기("0", "broadcast.room.info.view.success", "방송방 정보 보기 성공 시"),
    방정보보기_회원번호아님("-1", "broadcast.room.info.view.not.member.number","회원 번호가 아닐 시"),
    방정보보기_해당방없음("-2", "broadcast.room.info.view.room.no", "해당 방이 없을 시"),
    방정보보기_실패("C006", "broadcast.room.info.view.fail", "방송방 정보 보기 실패 시"),
    //방송진행여부체크
    방송진행여부체크_방송방없음("0", "broadcasting.none.success", "방송중인 방이없음"),
    방송진행여부체크_방송중("1", "broadcasting.ing.success", "방송중"),
    방송진행여부체크_비정상("2", "broadcasting.disconnect.success", "비정상종료된 방송 존재"),
    방송진행여부체크_요청회원아님("-1", "broadcasting.member.no", "요청회원이 아님"),
    방송진행여부체크_실패("C006", "broadcasting.fail", "방송중인 방이없음"),
    //방송방 사연 등록
    방송방사연등록성공("0", "broadcast.room.story.add.success", "방송방 사연 등록 성공 시"),
    방송방사연플러스등록성공("0", "broadcast.room.story.plus.add.success", "방송방 사연 플러스 등록 성공 시"),
    방송방사연등록_회원아님("-1", "broadcast.room.story.add.no", "방송방 사연 등록 회원이 아닐 시"),
    방송방사연등록_해당방이없음("-2", "broadcast.room.story.add.room.no", "방송방 사연등록 해당 방이 없을 시"),
    방송방사연등록_방참가자가아님("-3", "broadcast.room.story.add.member.no", "방송방 사연등록 방참가자가 아닐 시"),
    방송방사연등록_1분에한번등록가능("-4", "broadcast.room.story.add.time", "방송방 사연등록 1분에 한번 씩 등록 가능"),
    방송방사연등록_파라미터에러("-5", "broadcast.room.story.add.param", "방송방 사연등록 실패 시"),
    방송방사연등록오류("C006", "broadcast.room.story.add.fail", "방송방 사연등록 실패 시"),
    //방송방 사연 조회
    방송방사연조회성공("C001", "broadcast.room.story.sel.success", "방송방 사연조회 성공 시"),
    방송방사연조회_등록된사연없음("0", "broadcast.room.story.sel.no.success", "방송방 사연조회 등록된 사연 없을 시"),
    방송방사연조회_회원아님("-1", "broadcast.room.story.sel.member.no", "방송방 사연조회 회원이 아닐 시"),
    방송방사연조회_해당방이없음("-2", "broadcast.room.story.room.no", "방송방 사연조회 해당 방이 없을 시"),
    방송방사연조회_방참가자가아님("-3", "broadcast.room.story.member.no", "방송방 사연조회 방 참가자가 아닐 시"),
    방송방사연조회오류("C006", "broadcast.room.story.sel.fail", "방송방 사연조회 실패 시"),
    //방송방 사연 삭제
    방송방사연삭제성공("0", "broadcast.room.story.del.success", "방송방 사연삭제 성공 시"),
    방송방사연삭제_회원아님("-1", "broadcast.room.story.del.member.no", "방송방 사연삭제 회원 아닐 시"),
    방송방사연삭제_해당방이없음("-2", "broadcast.room.story.del.room.no", "방송방 사연삭제 해당 방 없을 시"),
    방송방사연삭제_방참가자가아님("-3", "broadcast.room.story.member.number.no", "방송방 사연삭제 방참가자 아닐 시"),
    방송방사연삭제_삭제권한없음("-4", "broadcast.room.story.member.authorization.no", "방송방 사연삭제 권한 없을 시"),
    방송방사연삭제_사연인덱스오류("-5", "broadcast.room.story.del.index.error", "방송방 사연삭제 인덱스 오류 발생 시"),
    방송방사연삭제오류("C006", "broadcast.room.story.del.fail", "방송방 사연삭제 실패 시"),
    //방송좋아요
    좋아요("0", "broadcast.like.success", "좋아요 성공 시"),
    좋아요_취소("0", "broadcast.cancel.success", "좋아요 취소 성공 시"),
    좋아요_회원아님("-1", "broadcast.like.member.no", "회원이 아닐 시"),
    좋아요_해당방송없음("-2", "broadcast.like.room.no", "해당 방송이 없을 시"),
    좋아요_방송참가자아님("-3", "broadcast.like.room.in.no", "방송 참가자가 아닐 시"),
    좋아요_이미했음("-4", "broadcast.like.already", "좋아요 이미 했을 시"),
    좋아요_하지않음("-4", "broadcast.cancel.already", "좋아요 하지 않았을 시"),
    좋아요_실패("C006", "broadcast.like.fail", "좋아요 실패 시"),
    좋아요_취소실패("C006", "broadcast.cancel.fail", "좋아요 취소 실패 시"),
    //방송방 공유하기 링크체크
    링크체크_성공("0", "broadcast.share.link.success", "링크체크 성공 시"),
    링크체크_회원아님("-1", "broadcast.share.link.member.no", "회원 아닐 시"),
    링크체크_해당방이없음("-2", "broadcast.share.link.room.no", "해당 방이 없을 시"),
    링크체크_방이종료되어있음("-3", "broadcast.share.link.room.end", "방이 종료 되어 있을시"),
    링크체크_실패("C006", "broadcast.share.link.fail", "링크체크 실패 시"),
    //방송방 게스트지정
    게스트지정("0", "broadcast.guest.add.success", "게스트 지정 성공 시"),
    게스트지정_회원아님("-1", "broadcast.guest.add.member.no", "회원이 아닐 시"),
    게스트지정_해당방이없음("-2", "broadcast.guest.add.room.no", "해당 방송이 없을 시"),
    게스트지정_방이종료되었음("-3", "broadcast.guest.add.room.end", "방이 종료되어 있을 시"),
    게스트지정_방소속_회원아님("-4", "broadcast.guest.add.room.join.no", "방에 소속된 회원이 아닐 시"),
    게스트지정_방장아님("-5", "broadcast.guest.add.bj.no", "요청회원이 방장이 아닐 시"),
    게스트지정_방소속_회원아이디아님("-6", "broadcast.guest.add.room.join.id.no", "방에 소속된 회원아이디가 아닐 시"),
    게스트지정_불가("-7", "broadcast.guest.add.no", "게스트 지정(이미 게스트 또는 방장 권한) 안될 시"),
    게스트지정_초대수락상태아님("-8", "broadcast.guest.add.no.invite.ok", "초대수락 상태가 아닐 시"),
    게스트지정_초과("-10", "broadcast.guest.add.limit", "게스트 수 초과 시"),
    게스트지정_실패("C006", "broadcast.guest.add.fail", "게스트 지정 실패 시"),
    //방송방 게스트 취소
    게스트취소("0", "broadcast.guest.cancel.success", "게스트 취소 성공 시"),
    게스트취소_회원아님("-1", "broadcast.guest.cancel.member.no", "회원이 아닐 시"),
    게스트취소_해당방이없음("-2", "broadcast.guest.cancel.room.no", "해당 방송이 없을 시"),
    게스트취소_방이종료되었음("-3", "broadcast.guest.cancel.room.end", "방이 종료되어 있을 시"),
    게스트취소_방소속_회원아님("-4", "broadcast.guest.cancel.room.join.no", "방에 소속된 회원이 아닐 시"),
    게스트취소_방장아님("-5", "broadcast.guest.cancel.bj.no", "요청회원이 방장이 아닐 시"),
    게스트취소_방소속_회원아이디아님("-6", "broadcast.guest.cancel.room.join.id.no", "방에 소속된 회원아이디가 아닐 시"),
    게스트취소_불가("-7", "broadcast.guest.cancel.no", "게스트가 아닐 시 "),
    게스트취소_실패("C006", "broadcast.guest.cancel.fail", "게스트 취소 실패 시"),
    //방송방 참여자 강제퇴장 시키기
    강제퇴장("0", "broadcast.kickout.success", "강제퇴장 성공 시"),
    강제퇴장_회원아님("-1", "broadcast.kickout.member.no", "회원이 아닐 시"),
    강제퇴장_해당방이없음("-2", "broadcast.kickout.room.no", "해당 방송이 없을 시"),
    강제퇴장_방이종료되었음("-3", "broadcast.kickout.room.end", "방이 종료되어 있을 시"),
    강제퇴장_요청회원_방소속회원아님("-4", "broadcast.kickout.request.room.join.no", "요청회원이 방에 소속된 회원이 아닐 시"),
    강제퇴장_권한없음("-5", "broadcast.kickout.authorization.not", "강제퇴장 권한 없을 시"),
    강제퇴장_대상회원_방소속회원아님("-6", "broadcast.kickout.object.room.join.no", "대상회원이 방에 소속된 회원이 아닐 시"),
    강제퇴장_게스트이상불가("-7", "broadcast.kickout.guest.not", "게스트 이상 강제퇴장 불가 시"),
    강제퇴장_매니저가매니저("-8", "broadcast.kickout.manager.manager", "매니저가 매니저 강퇴시"),
    강제퇴장_운영자("-99", "broadcast.kickout.admin", "운영자 강퇴시"),
    강제퇴장_실패("C006", "broadcast.kickout.fail", "강제퇴장 실패 시"),
    //유저
    매니저지정("3001", "broadcast.user.manager.add", "매니저 지정 시"),
    게스트초대("3003", "broadcast.user.guest.invite", "게스트 초대 시"),
    게스트초대수락("3005", "broadcast.user.guest.join", "게스트 초대 수락 시"),
    게스트신청("3006", "broadcast.user.guest.apply", "게스트 신청 시"),
    //방송방 공지사항 가져오기
    공지가져오기성공("0", "broadcast.room.notice.select.success", "공지사항 가져오기 성공 시"),
    공지가져오기실패_정상회원이아님("-1", "broadcast.room.notice.member.no", "정상회원이 아닐 시"),
    공지가져오기실패_해당방이없음("-2", "broadcast.room.notice.room.no", "해당 방이 없음"),
    공지가져오기실패_방참가자가아님("-3", "broadcast.room.notice.join.member.no", "방 참가자가 아닐 시"),
    공지가져오기실패_조회에러("C006", "broadcast.room.notice.fail", "공지사항 가져오기 실패 시"),
    //방송방 공지사항 입력/수정
    공지입력수정성공("0", "broadcast.room.notice.edit.success", "공지사항 입력/수정 성공 시"),
    공지입력수정실패_정상회원이아님("-1", "broadcast.room.notice.edit.member.no", "정상회원이 아닐 시"),
    공지입력수정실패_해당방이없음("-2", "broadcast.room.notice.edit.room.no", "해당 방이 없음"),
    공지입력수정실패_방참가자가아님("-3", "broadcast.room.notice.edit.join.member.no", "방 참가자가 아닐 시"),
    공지입력수정실패_공지권한없음("-4", "broadcast.room.notice.edit.authorization.not", "공지 입력/수정 권한이 없을 시"),
    공지입력수정실패_입력수정에러("C006", "broadcast.room.notice.edit.fail", "공지사항 입력/수정 실패 시"),
    //방송방 공지사항 삭제
    공지삭제하기성공("0", "broadcast.room.notice.delete.success", "공지사항 삭제 성공 시"),
    공지삭제하기실패_정상회원이아님("-1", "broadcast.room.notice.delete.member.no", "정상회원이 아닐 시"),
    공지삭제하기실패_해당방이없음("-2", "broadcast.room.notice.delete.room.no", "해당 방이 없을 시"),
    공지삭제하기실패_방참가자가아님("-3", "broadcast.room.notice.delete.join.member.no", "방 참가자가 아닐 시"),
    공지삭제하기실패_공지삭제권한없음("-4", "broadcast.room.notice.delete.authorization.not", "공지 삭제 권한이 없을 시"),
    공지삭제하기실패_삭제에러("C006", "broadcast.room.notice.delete.fail", "공지사항 삭제 실패 시"),
    //방송방 선물하기
    DJ_몰래_선물하기성공("0", "broadcast.room.gift.dj.secret.success", "몰래 선물하기 성공 시"),
    게스트_몰래_선물하기성공("0", "broadcast.room.gift.guest.secret.success", "몰래 선물하기 성공 시"),
    DJ_선물하기성공("0", "broadcast.room.gift.dj.success", "DJ에게 선물하기 성공 시"),
    게스트_선물하기성공("0", "broadcast.room.gift.guest.success", "게스트에게 선물하기 성공 시"),
    선물하기성공("0", "broadcast.room.gift.success", "선물하기 성공 시"),
    선물하기_요청회원_번호비정상("-1", "broadcast.room.gift.member.number.error", "요청회원 번호가 비정상일 시"),
    선물하기_해당방없음("-2", "broadcast.room.gift.no.room", "해당 방 없을 시"),
    선물하기_해당방종료("-3", "broadcast.room.gift.room.end", "해당 방이 종료되었을 시"),
    선물하기_요청회원_해당방청취자아님("-4", "broadcast.room.gift.member.join.no", "요청회원이 해당방 청취자가 아닐 시"),
    선물하기_받는회원_해당방에없음("-5", "broadcast.room.gift.member.no", "선물받을 회원이 해당방에 없을 시"),
    선물하기_없는대상("-6", "broadcast.room.gift.member.error", "선물할 수 없는 대상 시"),
    선물하기_아이템번호없음("-7", "broadcast.room.gift.item.number.no", "선택된 아이템이 없을 시"),
    선물하기_달부족("-8", "broadcast.room.gift.limit.dal", "달이 부족할 시"),
    선물하기_TTS_성우없음("-9", "broadcast.room.gift.tts.noActor", "요청하는 성우가 없을 시"),
    선물하기_TTS_변환가능문자없음("-10", "broadcast.room.gift.tts.change.fail", "변환 가능 문자가 없을 시"),
    선물하기_실패("C006", "broadcast.room.gift.fail", "선물하기 실패 시"),
    //방송방 부스터
    부스터성공("0", "broadcast.room.booster.success", "부스터 성공 시"),
    부스터_요청회원_번호비정상("-1", "broadcast.room.booster.member.number.error", "요청회원 번호가 비정상일 시"),
    부스터_해당방없음("-2", "broadcast.room.booster.no.room", "해당 방 없을 시"),
    부스터_해당방종료("-3", "broadcast.room.booster.room.end", "해당 방이 종료되었을 시"),
    부스터_요청회원_해당방청취자아님("-4", "broadcast.room.booster.member.join.no", "요청회원이 해당방 청취자가 아닐 시"),
    부스터_아이템번호없음("-5", "broadcast.room.booster.item.number.no", "아이템번호가 없을 시"),
    부스터_사용불가능아이템번호("-6", "broadcast.room.booster.item.number.error", "사용할 수 없는 아이템번호일 시"),
    부스터_달부족("-8", "broadcast.room.booster.dal.shortage", "달이 부족할 시"),
    부스터_실패("C006", "broadcast.room.booster.fail", "부스터 실패 시"),
    //방송방 순위 아이템사용 조회
    순위아이템사용_조회성공("0", "broadcast.room.live.rank.info.success", "순위 아이템사용 조회 성공 시"),
    순위아이템사용_요청회원_번호비정상("-1", "broadcast.room.live.rank.member.number.error", "요청회원 번호가 비정상일 시"),
    순위아이템사용_해당방없음("-2", "broadcast.room.live.rank.no.room", "해당 방 없을 시"),
    순위아이템사용_해당방종료("-3", "broadcast.room.live.rank.room.end", "해당 방이 종료되었을 시"),
    순위아이템사용_요청회원_해당방청취자아님("-4", "broadcast.room.live.rank.member.join.no", "요청회원이 해당방 청취자가 아닐 시"),
    순위아이템사용_조회실패("C006", "broadcast.room.live.rank.info.fail", "순위 아이템사용 조회 실패 시"),
    //방송방 매니저지정
    고정매니저지정_성공("0", "broadcast.room.fix.manager.add.success", "고정 매니저지정 성공 시"),
    매니저지정_성공("0", "broadcast.room.manager.add.success", "매니저지정 성공 시"),
    매니저지정_회원아님("-1", "broadcast.room.manager.add.member.no", "회원이 아닐 시"),
    매니저지정_해당방이없음("-2", "broadcast.room.manager.add.no.room", "해당 방 없을 시"),
    매니저지정_방이종료되었음("-3", "broadcast.room.manager.add.room.end", "방이 종료되었을 시"),
    매니저지정_요청회원_방소속아님("-4", "broadcast.room.manager.add.member.join.no", "요청회원이 방 소속이 아닐 시"),
    매니저지정_요청회원_방장아님("-5", "broadcast.room.manager.add.member.not.bj", "요청회원이 방장이 아닐 시"),
    매니저지정_대상회원아이디_방소속아님("-6", "broadcast.room.manager.add.member.not.join.id", "대상 회원아이디가 방 소속이 아닐 시"),
    매니저지정_불가("-7", "broadcast.room.manager.add.error", "매니저지정 불가 시(이미 매니저거나 방장 권한)"),
    매니저지정_인원제한("-8", "broadcast.room.manager.add.limit", "매니저 수 제한 시"),
    고정매니저지정_인원제한("-10", "broadcast.room.fix.manager.add.limit", "고정 매니저 수 제한 시"),
    고정매니저지정_이미지정("-11", "broadcast.room.fix.manager.add.already", "고정 매니저 이미 지정되어있을 시"),
    매니저지정_구분타입_오류("-12", "broadcast.room.manager.add.type.error", "매니저 구분타입 오류 시"),
    매니저지정_관리자("-99", "broadcast.room.manager.add.admin", "운영자 추가시"),
    매니저지정_실패("C006", "broadcast.room.manager.add.fail", "매니저지정 실패 시"),
    //방송방 매니저취소
    고정매니저취소_성공("0", "broadcast.room.fix.manager.del.success", "고정 매니저취소 성공 시"),
    매니저취소_성공("0", "broadcast.room.manager.del.success", "매니저취소 성공 시"),
    매니저취소_회원아님("-1", "broadcast.room.manager.del.member.no", "회원이 아닐 시"),
    매니저취소_해당방이없음("-2", "broadcast.room.manager.del.no.room", "해당 방 없을 시"),
    매니저취소_방이종료되었음("-3", "broadcast.room.manager.del.room.end", "방이 종료되었을 시"),
    매니저취소_요청회원_방소속아님("-4", "broadcast.room.manager.del.member.join.no", "요청회원이 방 소속이 아닐 시"),
    매니저취소_요청회원_방장아님("-5", "broadcast.room.manager.del.member.not.bj", "요청회원이 방장이 아닐 시"),
    매니저취소_대상회원아이디_방소속아님("-6", "broadcast.room.manager.del.member.not.join.id", "대상 회원아이디가 방 소속이 아닐 시"),
    매니저취소_매니저아님("-7", "broadcast.room.manager.del.no", "매니저 아닐 시"),
    매니저취소_구분타입_오류("-8", "broadcast.room.manager.del.type.error", "매니저 구분타입 오류 시"),
    매니저취소_실패("C006", "broadcast.room.manager.del.fail", "매니저취소 실패 시"),
    //방송방 스트림아이디 가져오기
    스트림아이디_조회성공("0", "broadcast.room.stream.id.success", "스트림아이디 조회 성공 시"),
    스트림아이디_회원아님("-1", "broadcast.room.stream.id.member.no", "회원이 아닐 시"),
    스트림아이디_해당방없음("-2", "broadcast.room.stream.id.no.room", "해당 방 없을 시"),
    스트림아이디_요청회원_방소속아님("-3", "broadcast.room.stream.id.member.join.no", "요청회원이 방 소속이 아닐 시"),
    스트림아이디_조회실패("C006", "broadcast.room.stream.id.fail", "스트림아이디 조회 실패 시"),
    //방송방 선물받은내역 조회
    선물받은내역조회("C001", "broadcast.room.gift.history.success", "선물받은내역 조회 성공 시"),
    선물받은내역없음("0", "broadcast.room.gift.history.no.success", "선물받은내역 없을 시"),
    선물내역조회_회원번호정상아님("-1", "broadcast.room.gift.history.member.number.error", "회원번호가 정상이 아닐 시"),
    선물내역조회_해당방없음("-2", "broadcast.room.gift.history.no.room", "해당 방 없을 시"),
    선물받은내역조회_실패("C006", "broadcast.room.gift.history.fail", "선물받은내역 조회 실패 시"),
    //방송방 회원정보 조회
    방송방회원정보조회_성공("0", "broadcast.room.member.info.select.success", "방송방 회원정보 조회 성공 시"),
    방송방회원정보조회_요청회원_번호비정상("-1", "broadcast.room.member.info.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    방송방회원정보조회_대상회원_번호비정상("-2", "broadcast.room.member.info.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    방송방회원정보조회_종료된방("-3", "broadcast.room.member.info.no.room", "종료된 방일 시"),
    방송방회원정보조회_대상회원_방에없음("-4", "broadcast.room.member.info.join.no", "대상 회원이 방에 없을 시"),
    방송방회원정보조회_차단회원_접근불가("-5", "broadcast.room.member.info.black.list.no","차단회원 접근 불가 시"),
    방송방회원정보조회_실패("C006", "broadcast.room.member.info.select.fail", "방송방 회원정보 조회 실패 시"),
    //라이브 방송 검색
    라이브방송검색_성공("C001", "live.broadcast.search.success", "라이브 방송 검색 성공 시"),
    라이브방송검색_결과없음("0", "live.broadcast.search.no.success", "결과 없을 시"),
    라이브방송검색_실패("C006", "live.broadcast.search.fail", "라이브 방송 검색 실패 시"),
    //방송방 상태 변경
    방송방상태변경_성공("0", "broadcast.room.state.update.success", "방송방 상태 변경 성공 시"),
    방송방상태변경_회원아님("-1", "broadcast.room.state.update.member.error", "요청회원이 정상회원 아닐 시"),
    방송방상태변경_해당방이없음("-2", "broadcast.room.state.update.no.room", "해당 방 없을 시"),
    방송방상태변경_방이종료되었음("-3", "broadcast.room.state.update.end.room", "방이 종료되었을 시"),
    방송방상태변경_요청회원_방소속아님("-4", "broadcast.room.state.update.join.not.member", "요청 회원이 방 소속 아닐 시"),
    방송방상태변경_요청회원_방장아님("-5", "broadcast.room.state.update.not.bj", "요청 회원이 방장 아닐 시"),
    방송방상태변경_실패("C006", "broadcast.room.state.update.fail", "방송방 상태 변경 실패 시"),
    //방송시간 연장
    시간연장성공("0", "broadcast.time.extend.success", "방송시간 연장 성공 시"),
    시간연장_회원아님("-1", "broadcast.time.extend.member.number.error", "회원 번호 아닐 시"),
    시간연장_방번호없음("-2", "broadcast.time.extend.room.number.no", "방번호 없을 시"),
    시간연장_종료된방("-3", "broadcast.time.extend.room.exit", "종료된 방일 시"),
    시간연장_이미한번연장("-4", "broadcast.time.extend.already.once", "이미 한번 연장 했을 시"),
    시간연장_남은시간_5분안됨("-5", "broadcast.time.extend.not.remain.5min", "남은시간이 5분 아닐 시"),
    시간연장실패("C006", "broadcast.time.extend.fail", "방송시간 연장 실패 시"),
    //방송방이미지 초기화
    방송방이미지초기화_성공("0", "room.image.init.success", "방송방이미지 초기화 성공 시"),
    방송방이미지초기화_실패("C006", "room.image.init.fail", "방송방이미지 초기화 실패 시"),
    //방송제목 초기화
    방송제목초기화_성공("0", "broadcast.title.init.success", "방송제목 초기화 성공 시"),
    방송제목초기화_실패("C006", "broadcast.title.init.fail", "방송제목 초기화 실패 시"),
    //생방송 메시지 목록 조회
    생방송메시지조회_성공("C001", "live.chat.select.success", "생방송 메시지 조회 성공 시"),
    생방송메시지조회_성공_데이터없음("C002", "live.chat.no.data.success", "생방송 메시지 조회 성공 시"),
    생방송메시지조회_실패("C006", "live.chat.select.fail", "생방송 메시지 조회 실패 시"),
    //생방송 청취자 강제퇴장
    생방청취자강제퇴장_성공("0", "broadcast.listen.forced.success", "생방송 청취자 강제퇴장 성공 시"),
    생방청취자강제퇴장_회원아님("-1", "broadcast.listen.forced.no.member", "생방송 청취자 강제퇴장 회원 아닐 시"),
    생방청취자강제퇴장_방없음("-2", "broadcast.listen.forced.no.room", "생방송 청취자 강제퇴장 방 없을 시"),
    생방청취자강제퇴장_종료된방("-3", "broadcast.listen.forced.end.room", "생방송 청취자 강제퇴장 종료된 방일 시"),
    생방청취자강제퇴장_청취자아님("-4", "broadcast.listen.forced.no.listener", "생방송 청취자 강제퇴장 청취자 아닐 시"),
    생방청취자강제퇴장_퇴장한회원("-5", "broadcast.listen.forced.no.forced", "생방송 청취자 강제퇴장 이미 퇴장한 회원 일 시"),
    //방송방메시지 발송
    방송방메시지발송_성공("0","system.message.insert.success", "방송방메시지발송 성공 시"),
    방송방메시지발송_에러("C006", "system.message.fail.server.error", "방송방메시지발송 실패 시"),
    방송방메시지발송_타겟미지정("C006", "system.message.fail.no.data", "방송방메시지발송 시 타겟 방정보가 없는 경우"),
    //방송방 왕회장 & 팬 랭킹 3 조회
    방송방_팬랭킹조회_성공("C001", "broadcast.fan.ranking.select.success", "팬 랭킹 조회 성공 시"),
    방송방_팬랭킹조회_팬없음("0", "broadcast.fan.ranking.no.fan.success", "팬 없을 시"),
    방송방_팬랭킹조회_방번호없음("-1", "broadcast.fan.ranking.room.number.no", "방 번호 없을 시"),
    방송방_팬랭킹조회_방종료됨("-1", "broadcast.fan.ranking.room.end", "방이 종료되었을 시"),
    방송방_팬랭킹조회_실패("C006", "broadcast.fan.ranking.select.fail", "팬 랭킹 조회 실패 시"),
    //마이페이지 방송설정 제목추가
    방송설정_제목추가_성공("0", "mypage.broadcast.title.add.success", "방송제목 추가 성공 시"),
    방송설정_제목추가_회원아님("-1", "mypage.broadcast.title.add.member.number.error", "요청 회원번호 정상 아닐 시"),
    방송설정_제목추가_제한("-2", "mypage.broadcast.title.add.limit3", "방송제목 추가 제한 시"),
    방송설정_제목추가_실패("C006", "mypage.broadcast.title.add.fail", "방송제목 추가 실패 시"),
    //마이페이지 방송설정 제목수정
    방송설정_제목수정_성공("0", "mypage.broadcast.title.edit.success", "방송제목 수정 성공 시"),
    방송설정_제목수정_회원아님("-1", "mypage.broadcast.title.edit.member.number.error", "요청 회원번호 정상 아닐 시"),
    방송설정_제목수정_번호없음("-2", "mypage.broadcast.title.edit.no.order.number", "방송제목 수정 번호없을 시"),
    방송설정_제목수정_오류("C006", "mypage.broadcast.title.edit.fail", "방송제목 수정 실패 시"),
    //마이페이지 방송설정 제목조회
    방송설정_제목조회_성공("C001", "mypage.broadcast.title.select.success", "방송제목 조회 성공 시"),
    방송설정_제목조회_없음("0", "mypage.broadcast.title.select.no.success", "방송제목 조회 없을 시"),
    방송설정_제목조회_회원아님("-1", "mypage.broadcast.title.select.member.number.error", "요청 회원번호 정상 아닐 시"),
    방송설정_제목조회_실패("C006", "mypage.broadcast.title.select.fail", "방송제목 수정 실패 시"),
    //마이페이지 방송설정 제목삭제
    방송설정_제목삭제_성공("0", "mypage.broadcast.title.delete.success", "방송제목 삭제 성공 시"),
    방송설정_제목삭제_회원아님("-1", "mypage.broadcast.title.delete.member.number.error", "요청 회원번호 정상 아닐 시"),
    방송설정_제목삭제_번호없음("-2", "mypage.broadcast.title.delete.no.order.number", "방송제목 삭제 번호없을 시"),
    방송설정_제목삭제_실패("C006", "mypage.broadcast.title.delete.fail", "방송제목 삭제 실패 시"),
    //마이페이지 방송설정 인사말추가
    방송설정_인사말추가_성공("0", "mypage.broadcast.welcome.msg.add.success", "인사말 추가 성공 시"),
    방송설정_인사말추가_회원아님("-1", "mypage.broadcast.welcome.msg.add.member.number.error", "요청 회원번호 정상 아닐 시"),
    방송설정_인사말추가_제한("-2", "mypage.broadcast.welcome.msg.add.limit3", "인사말 추가 제한 시"),
    방송설정_인사말추가_실패("C006", "mypage.broadcast.welcome.msg.add.fail", "인사말 추가 실패 시"),
    //마이페이지 방송설정 인사말수정
    방송설정_인사말수정_성공("0", "mypage.broadcast.welcome.msg.edit.success", "인사말 수정 성공 시"),
    방송설정_인사말수정_회원아님("-1", "mypage.broadcast.welcome.msg.edit.member.number.error", "요청 회원번호 정상 아닐 시"),
    방송설정_인사말수정_번호없음("-2", "mypage.broadcast.welcome.msg.edit.no.order.number", "인사말 수정 번호없을 시"),
    방송설정_인사말수정_오류("C006", "mypage.broadcast.welcome.msg.edit.fail", "인사말 수정 실패 시"),
    //마이페이지 방송설정 인사말조회
    방송설정_인사말조회_성공("C001", "mypage.broadcast.welcome.msg.select.success", "인사말 조회 성공 시"),
    방송설정_인사말조회_없음("0", "mypage.broadcast.welcome.msg.select.no.success", "인사말 조회 없을 시"),
    방송설정_인사말조회_회원아님("-1", "mypage.broadcast.welcome.msg.select.member.number.error", "요청 회원번호 정상 아닐 시"),
    방송설정_인사말조회_실패("C006", "mypage.broadcast.welcome.msg.select.fail", "인사말 수정 실패 시"),
    //마이페이지 방송설정 인사말삭제
    방송설정_인사말삭제_성공("0", "mypage.broadcast.welcome.msg.delete.success", "인사말 삭제 성공 시"),
    방송설정_인사말삭제_회원아님("-1", "mypage.broadcast.welcome.msg.delete.member.number.error", "요청 회원번호 정상 아닐 시"),
    방송설정_인사말삭제_번호없음("-2", "mypage.broadcast.welcome.msg.delete.no.order.number", "인사말 삭제 번호없을 시"),
    방송설정_인사말삭제_실패("C006", "mypage.broadcast.welcome.msg.delete.fail", "인사말 삭제 실패 시"),
    //방송방 채팅 얼리기
    얼리기_성공("0", "broadcast.room.freeze.true.success", "얼리기 성공 시"),
    얼리기_해제("0", "broadcast.room.freeze.false.success", "얼리기 해제 시"),
    관리자_얼리기_성공("0", "broadcast.room.freeze.admin.true.success", "얼리기 성공 시"),
    관리자_얼리기_해제("0", "broadcast.room.freeze.admin.false.success", "얼리기 해제 시"),
    관리자_얼리기중_얼리기시도("0", "broadcast.room.freeze.already.admin", "관리자가 채팅 얼리기 중 DJ 얼리기 시도 시"),
    관리자_얼리기중_DJ해제실패("0", "broadcast.room.freeze.admin.state", "관리자가 얼리기 중 얼리기 해제 시도 시"),
    얼리기_회원아님("-1", "broadcast.room.freeze.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    얼리기_방번호없음("-2", "broadcast.room.freeze.no.room.number", "방 번호 없을 시"),
    얼리기_종료된방("-3", "broadcast.room.freeze.end.room", "종료된 방일 시"),
    얼리기_요청회원_방에없음("-4", "broadcast.room.freeze.member.not.in.room", "요청회원 방에 없을 시"),
    얼리기_불가상태("-5", "broadcast.room.freeze.impossible", "얼리기 불가 상태"),
    얼리기_실패("C006", "broadcast.room.freeze.fail", "얼리기 실패 시"),
    //방송종료 요약
    방송종료요약_성공("0", "broad.summary.success", "조회 성공"),
    방송종료요약_회원아님("-1", "broad.summary.no.member", "회원정보 찾을 수 없음"),
    방송종료요약_방없음("-2", "broad.summary.no.room", "방정보가 없거나 종료가 아닐때(종료여부는 DJ만)"),
    방송종료요약_실패("C006", "broad.summary.fail", "조회 실패"),
    //방송방 좋아요 내역
    좋아요내역조회_성공("C001", "broadcast.good.history.select.success", "좋아요 내역조회 성공 시"),
    좋아요내역조회_없음("0", "broadcast.good.history.no.success", "좋아요 내역 없을 시"),
    좋아요내역조회_회원번호정상아님("-1", "broadcast.good.history.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    좋아요내역조회_해당방없음("-2", "broadcast.good.history.select.no.room", "해당 방이 없을 시"),
    좋아요내역조회_미참여중("-3", "broadcast.good.history.select.not.join.member", "요청회원이 방송방에 없을 시"),
    좋아요내역조회_권한없음("-4", "broadcast.good.history.select.no.auth", "좋아요 내역조회 권한 없을 시"),
    좋아요내역조회_실패("C006", "broadcast.good.history.select.fail", "좋아요 내역조회 실패 시"),
    //이어하기
    이어하기가능("C100", "broadcast.continue.possibility.success", "이어하기 가능 시"),
    이어하기_성공("0", "broadcast.continue.success", "이어하기 성공 시"),
    이어하기_회원아님("-1", "broadcast.continue.member.number.error", "요청 회원번호가 회원이 아닐 시"),
    이어하기_방없음("-2", "broadcast.continue.no.room", "이어하기할 방이 없을 시"),
    이어하기_연장한방송("-3", "broadcast.continue.extend.room", "연장한 방송이라 이어하기 불가"),
    이어하기_종료시간5분지남("-4", "broadcast.continue.end.time.5min.pass", "종료시간이 5분 지남"),
    이어하기_남은시간5분안됨("-5", "broadcast.continue.remaining.time.5min.limit", "남은시간이 5분 안됨"),
    이어하기_청취중_방송생성("-7", "broadcast.continue.already.listener", "청취중 방송 생성시 에러코드"),
    이어하기_실패("C006", "broadcast.continue.fail", "이어하기 실패 시"),
    //방송방 배지 교체 체크
    배지교체대상체크_없음("0", "broadcast.badge.change.checnk.no.success", "배지 교체 체크대상 없을 시"),
    배지교체대상체크_성공("C001", "broadcast.badge.change.check.success", "배지 교체대상 체크 성공 시"),
    배지교체대상체크_실패("C006", "broadcast.badge.change.check.fail", "배지 교체대상 체크 실패 시");



    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    BroadcastStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
