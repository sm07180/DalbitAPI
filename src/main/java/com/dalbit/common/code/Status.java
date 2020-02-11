package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum Status {

    //공통
    조회("C001", "read.success", "조회"),
    수정("C002", "update.success", "수정"),
    생성("C003", "create.success", "생성"),
    삭제("C004", "delete.success", "삭제"),
    파라미터오류("C005", "param.error", "파라미터 오류 시"),
    비즈니스로직오류("C006", "business.error", "비즈니스로직 오류 시"),

    //로그인
    로그인성공("0", "login.success", "로그인 성공 시"),
    로그인실패_회원가입필요("1", "login.join.need", "회원가입 필요 시"),
    로그인실패_패스워드틀림("-1", "login.fail", "로그인 실패 시 - 아이디/비밀번호가 틀릴 시"),
    로그인실패_파라메터이상("-2", "login.param.error", "로그인 실패 시 - 파라메터이상"),

    로그아웃성공("0", "logout.success", "로그아웃 성공 시"),
    로그아웃실패_진행중인방송("0", "logout.mybroadcast.ing", "로그아웃을 시도했지만 방송을 진행하고 있을 때"),

    //회원가입
    회원가입안됨("1","member.join.not", "회원가입 안된 상태 시"),
    회원가입성공("0", "member.join.success", "회원가입 성공 시"),
    회원가입실패_중복가입("-1", "member.join.already", "이미 회원가입된 상태 시"),
    회원가입실패_닉네임중복("-2", "member.join.nick.duplicate", "닉네임 중복 시"),
    회원가입실패_파라메터오류("-3", "param.error", "파라메터 오류 시"),
    회원가입오류("C006", "member.join.error", "회원가입 오류 시"),

    //닉네임중복체크
    닉네임중복("0", "member.join.nick.duplicate", "닉네임 중복 시"),
    닉네임사용가능("1", "member.join.nick.success", "닉네임 사용가능 시"),
    닉네임_파라메터오류("C005", "nick.name.error", "닉네임 파라메터 오류 시"),

    //비밀번호변경
    비밀번호변경실패_회원아님("0", "member.change.password.fail", "비밀번호 변경 실패 - 회원이 아닌경우"),
    비밀번호변경성공("1", "member.change.password.success", "비밀번호 변경 성공 시"),
    비밀번호변경오류("C006", "member.change.password.error", "비밀번호 변경 오류 시"),

    //프로필편집
    프로필편집성공("0", "member.edit.profile.success", "프로필 편집 성공 시"),
    프로필편집실패_회원아님("-1", "member.edit.profile.fail.notUser", "프로필 편집 실패 - 회원이 아닌경우"),
    프로필편집실패_닉네임중복("-2", "member.edit.profile.fail.duplicateNickName", "프로필 편집 실패 - 닉네임이 중복된 경우"),
    프로필편집오류("C006", "member.edit.profile.error", "프로필 편집 오류 시"),

    //회원팬등록
    팬등록성공("0", "member.insert.fanstar.success", "회원 팬 등록 성공 시"),
    팬등록_회원아님("-1", "member.insert.fan.no", "회원이 아닐 시"),
    팬등록_스타회원번호이상("-2", "member.insert.star.number.strange", "스타회원번호 이상 시"),
    팬등록_이미팬등록됨("-3", "member.insert.fan.already", "이미 팬 등록되어있을 시"),
    팬등록실패("C006", "member.insert.fanstar.fail", "회원 팬 등록 실패 시 "),

    //회원팬해제
    팬해제성공("0", "member.delete.fanstar.success", "팬 해제 성공 시"),
    팬해제_회원아님("-1", "member.delete.fan.no", "회원이 아닐 시"),
    팬해제_스타회원번호이상("-2", "member.delete.star.number.strange", "스타회원번호 이상 시"),
    팬해제_팬아님("-3", "member.delete.fan.not", "팬이 아닐 시"),
    팬해제실패("C006", "member.delete.fan.fail", "팬 해제 실패 시"),

    //회원정보보기 (프로필, 마이페이지 통합)
    회원정보보기_성공("0", "member.info.view.success", "회원 팬 등록 성공 시"),
    회원정보보기_회원아님("-1", "member.no", "회원이 아닐 시"),
    회원정보보기_대상아님("-2", "member.this.no", "회원 팬 등록 실패 시 "),
    회원정보보기_실패("C006", "member.info.view.fail", "회원정보보기 실패 시"),

    //회원 방송방 기본설정 조회
    방송방기본설정조회_성공("0", "mypage.broad.basic.success", "방송방 기본설정 조회 성공 시"),
    방송방기본설정조회_회원아님("-1", "mypage.broad.basic.no", "회원이 아닐 시"),
    방송방기본설정조회_오류("C006", "mypage.broad.basic.error", "회원 방송방 기본설정 조회 오류 시"),

    //회원 방송방 기본설정 수정
    방송방기본설정수정_성공("0", "mypage.broad.basic.edit.success", "방송방 기본설정 수정 성공 시"),
    방송방기본설정수정_회원아님("-1", "mypage.broad.basic.edit.no", "회원이 아닐 시"),
    방송방기본설정수정_오류("C006", "mypage.broad.basic.edit.error", "회원 방송방 기본설정 수정 오류 시"),

    //회원 신고하기
    회원신고성공("0", "mypage.member.report.add.success", "회원 신고 성공 시"),
    회원신고_요청회원번호_정상아님("-1", "mypage.member.report.add.mem.no", "요청 회원번호 정상 아닐 시"),
    회원신고_신고회원번호_정상아님("-2", "mypage.member.report.add.reported.no", "신고 회원번호 정상 아닐 시"),
    회원신고_이미_신고상태("-3", "mypage.member.reported", "이미 신고 상태 시"),
    회원신고오류("C006", "mypage.member.reported.error", "회원 신고 신청 중 오류 시"),

    //회원 차단하기
    회원차단성공("0", "mypage.member.block.success", "회원 차단 성공 시"),
    회원차단_요청회원번호_정상아님("-1", "mypage.member.block.mem.no", "요청 회원번호 정상 아닐 시"),
    회원차단_차단회원번호_정상아님("-2", "mypage.member.block.blocked.mem.no", "차단 회원번호 정상 아닐 시"),
    회원차단_이미_차단상태("-3", "mypage.member.block.blocked", "이미 차단 상태 시"),
    회원차단오류("C006", "mypage.member.block.error", "회원 차단 신청중 오류 시"),

    //회원 차단해제하기
    회원차단해제성공("0", "mypage.member.block.del.success", "회원 차단 해제 성공 시"),
    회원차단해제_요청회원번호_정상아님("-1", "mypage.member.block.del.mem.no", "요청 회원번호 정상 아닐 시"),
    회원차단해제_신고회원번호_정상아님("-2", "mypage.member.block.blocked.del.mem.no", "차단 회원번호 정상 아닐 시"),
    회원차단안된상태("-3", "mypage.member.block.no", "차단 안된 상태 시"),
    회원차단해제오류("C006", "mypage.member.block.del.error", "차단 해제 신청중 오류 시"),

    //회원 알림설정 조회하기
    알림설정조회_성공("0", "mypage.member.notify.sel.success", "알림 설정조회 성공 시"),
    알림설정조회_회원아님("-1", "mypage.member.notify.no", "알림설정 회원아닐 시"),
    알림설정조회오류("C006", "mypage.member.notify.error", "알림 설정 오류 시"),

    // 알림 설정
    알림설정수정_성공("0", "mypage.member.notify.edit.success", "알림 설정수정 성공 시"),
    알림설정수정_회원아님("-1", "mypage.member.notify.edit.no", "알림 설정수정 회원아닐 시"),
    알림설정수정오류("C006", "mypage.member.notify.edit.error", "알림 설정 오류 시"),

    //회원 방송방 빠른말 가져오기
    회원방송방빠른말조회_성공("0", "mypage.member.shorucut.success", "회원 방송방 빠른말 가져오기 성공 시"),
    회원방송방빠른말조회_회원아님("-1", "mypage.member.shorucut.no", "요청번호가 회원이 아닐 시"),
    회원방송방빠른말조회오류("C006", "mypage.member.shorucut.error", "회원 방송방 빠른말 가져오기 오류 시"),

    //회원방송방 빠른말 설정 수정/저장 하기
    회원방송방빠른말수정_성공("0", "mypage.member.shorucut.edit.success", "회원 방송방 빠른말 수정 성공 시"),
    회원방송방빠른말수정_회원아님("-1", "mypage.member.shorucut.edit.no", "요청번호가 회원이 아닐 시"),
    회원방송방빠른말수정오류("C006", "mypage.member.shorucut.edit.error", "회원 방송방 빠른말 수정 오류 시"),

    //회원 마이페이지 팬보드 댓글 달기
    팬보드_댓글달기성공("0", "fanboard.add.success", "댓글달기 성공 시"),
    팬보드_댓글달기실패_스타회원번호_회원아님("-1", "fanboard.add.fail.StarNo.notMember", "스타 회원번호가 회원이 아님"),
    팬보드_댓글달기실패_작성자회원번호_회원아님("-2", "fanboard.add.fail.writerNo.notMember", "작성자 회원번호가 회원이 아님"),
    팬보드_댓글달기실패_잘못된댓글그룹번호("-3", "fanboard.add.fail.wrong.groupNo", "댓글 그룹번호가 잘못된 번호"),
    팬보드_댓글달기실패_depth값_오류("-4", "fanboard.add.fail.wrong.depth", " depth 값 잘못됨"),
    팬보드_댓글달기실패_등록오류("C006", "fanboard.add.error", "팬보드 댓글 등록 실패"),

    //회원 마이페이지 팬보드 댓글 리스트
    팬보드조회성공("C001", "fanboard.list.comment.success", "팬보드 정보 조회 시"),
    팬보드_댓글없음("0", "fanboard.list.comment.no", "댓글이 없음"),
    팬보드_요청회원번호_회원아님("-1", "fanboard.list.requestNo.notMember", "요청 회원번호가 회원이 아님"),
    팬보드_스타회원번호_회원아님("-2", "fanboard.list.starNo.notMember","스타 회원번호가 회원이 아님"),
    팬보드_조회오류("C006", "fanboard.list.error", "팬보드 댓글 리스트 조회 실패"),

    //회원 마이페이지 팬보드 댓글 삭제
    팬보드_댓글삭제성공("0", "fanboard.delete.comment.success", "댓글 삭제 성공"),
    팬보드_댓글삭제실패_스타회원번호_회원아님("-1", "fanboard.delete.starNo.notMember", "스타 회원번호가 회원이 아님"),
    팬보드_댓글삭제실패_삭제자회원번호_회원아님("-2", "fanboard.delete.writerNo.notMember", "삭제자 회원번호가 회원이 아님"),
    팬보드_댓글삭제실패_댓글인덱스번호_잘못된번호("-3", "fanboard.delete.wrong.indexNo", "댓글 인덱스번호가 잘못된 번호"),
    팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름("-4", "fanboard.delete.notSame.IndexNoStarNo", "요청 인덱스번호의 스타 회원번호가 다름"),
    팬보드_댓글삭제실패_이미삭제됨("-5", "fanboard.delete.already.delete", "이미 삭제됨"),
    팬보드_댓글삭제실패_삭제권한없음("-6", "fanboard.delete.authorization.not", "삭제 권한이 없음"),
    팬보드_댓글삭제오류("C006", "fanboard.delete.error", "팬보드 댓글 삭제 실패"),

    //회원 마이페이지 팬보드 대댓글 보기
    팬보드_대댓글조회성공("C001", "fanboard.reply.comment.success", "팬보드 대댓글 조회 시"),
    팬보드_대댓글조회실패_대댓글없음("0", "fanboard.reply.comment.no", "대댓글 없음"),
    팬보드_대댓글조회실패_요청회원번호_회원아님("-1", "fanboard.reply.requestNo.notMember", "요청 회원번호가 회원이 아님"),
    팬보드_대댓글조회실패_스타회원번호_회원아님("-2", "fanboard.reply.starNo.notMember", "스타 회원번호가 회원이 아님"),
    팬보드_대댓글조회오류("C006", "fanboard.reply.error", "팬보드 대댓글 보기 실패"),

    //방송생성
    방송생성("0", "broadcast.room.start.success", "방송 생성 시"),
    방송생성_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송중인방존재("-2", "broadcast.room.existence", "방송중인 방이 있을 시"),
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
    방참가실패("C006", "broadcast.room.join.fail", "방송 참가 실패 시"),

    //방송나가기
    방송나가기("0", "broadcast.room.out.success", "방송 나가기 시"),
    방송나가기_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송나가기_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송나가기_종료된방송("-3", "broadcast.room.end", "종료된 방송일 시"),
    방송나가기_방참가자아님("-4", "broadcast.room.join.member.no", "방송 참가자 아닐 시"),
    방송나가기실패("C006", "broadcast.out.fail", "방송 나가기 실패 시"),

    //방송정보수정
    방송정보수정성공("0", "broadcast.room.edit.success", "방송정보 수정 성공 시"),
    방송정보수정_회원아님("-1", "broadcast.room.edit.member.no", "회원이 아닐 시"),
    방송정보수정_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송정보수정_해당방에없는회원번호("-3", "broadcast.room.member.number.no", "해당 방에 없는 회원번호일 시"),
    방송정보수정_수정권이없는회원("-4", "broadcast.room.member.edit.no", "수정권한이 없는 회원일 시"),
    방송정보수정실패("C006", "broadcast.room.edit.fail", "방송정보 수정 실패 시"),

    //방송리스트
    방송리스트조회("C001", "broadcast.room.list.select.success", "방송리스트 정보 조회 성공 시"),
    방송리스트없음("0", "broadcast.room.list.no", "방송리스트가 없을 시"),
    방송리스트_회원아님("-1", "broadcast.room.list.member.number.no", "회원번호가 아닐 시"),
    방송리스트조회_실패("C006", "broadcast.room.list.select.fail", "방송리스트 정보 조회 실패 시"),

    //방송참여자 리스트
    방송참여자리스트_조회("C001", "broadcast.room.list.member.select.success", "방송참여자리스트 정보 조회 시"),
    방송참여자리스트없음("0", "broadcast.room.member.list.no", "방송참여자 리스트가 없을 시"),
    방송참여자리스트_회원아님("-1", "broadcast.room.list.member.number", "회원번호가 아닐 시"),
    방송참여자리스트_방없음("-2", "broadcast.room.list.member.room.no", "해당 방이 없을 시"),
    방송참여자리스트조회_실패("C006", "broadcast.room.list.member.select.fail", "방송참여자 리스트 조회 실패 시"),

    //방송방 정보 보기
    방정보보기("0", "broadcast.room.info.view.success", "방송방 정보 보기 성공 시"),
    방정보보기_회원번호아님("-1", "broadcast.room.info.view.not.member.number","회원 번호가 아닐 시"),
    방정보보기_해당방없음("-2", "broadcast.room.info.view.room.no", "해당 방이 없을 시"),
    방정보보기_실패("C006", "broadcast.room.info.view.fail", "방송방 정보 보기 실패 시"),

    //방송방 사연 등록
    방송방사연등록성공("0", "broadcast.room.story.add.success", "방송방 사연 등록 성공 시"),
    방송방사연등록_회원아님("-1", "broadcast.room.story.add.no", "방송방 사연 등록 회원이 아닐 시"),
    방송방사연등록_해당방이없음("-2", "broadcast.room.story.add.room.no", "방송방 사연등록 해당 방이 없을 시"),
    방송방사연등록_방참가자가아님("-3", "broadcast.room.story.add.member.no", "방송방 사연등록 방참가자가 아닐 시"),
    방송방사연등록_10분에한번등록가능("-4", "broadcast.room.story.add.time", "방송방 사연등록 10분에 한번 씩 등록 가능"),
    방송방사연등록오류("C006", "broadcast.room.story.add.fail", "방송방 사연등록 실패 시"),

    //방송방 사연 조회
    방송방사연조회성공("C001", "broadcast.room.story.sel.success", "방송방 사연조회 성공 시"),
    방송방사연조회_등록된사연없음("0", "broadcast.room.story.sel.no", "방송방 사연조회 등록된 사연 없을 시"),
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
    좋아요_회원아님("-1", "broadcast.like.member.no", "회원이 아닐 시"),
    좋아요_해당방송없음("-2", "broadcast.like.room.no", "해당 방송이 없을 시"),
    좋아요_방송참가자아님("-3", "broadcast.like.room.in.no", "방송 참가자가 아닐 시"),
    좋아요_이미했음("-4", "broadcast.like.already", "좋아요 이미 했을 시"),
    좋아요_실패("C006", "broadcast.like.fail", "좋아요 실패 시"),

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
    게스트지정_불가("-7", "broadcast.guest.add.no", "게스트 지정(이미 게스트 또는 방장 권한) 안될 시 "),
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
    선물하기성공("0", "broadcast.room.gift.success", "선물하기 성공 시"),
    선물하기_요청회원_번호비정상("-1", "broadcast.room.gift.member.number.error", "요청회원 번호가 비정상일 시"),
    선물하기_해당방없음("-2", "broadcast.room.gift.no.room", "해당 방 없을 시"),
    선물하기_해당방종료("-3", "broadcast.room.gift.room.end", "해당 방이 종료되었을 시"),
    선물하기_요청회원_해당방청취자아님("-4", "broadcast.room.gift.member.join.no", "요청회원이 해당방 청취자가 아닐 시"),
    선물하기_받는회원_해당방에없음("-5", "broadcast.room.gift.member.no", "선물받을 회원이 해당방에 없을 시"),
    선물하기_없는대상("-6", "broadcast.room.gift.member.error", "선물할 수 없는 대상 시"),
    선물하기_아이템번호없음("-7", "broadcast.room.gift.item.number.no", "아이템번호가 없을 시"),
    선물하기_실패("C006", "broadcast.room.gift.fail", "선물하기 실패 시"),

   ;

    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    Status(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
