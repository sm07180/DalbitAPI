package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum Status {

    //공통
    파라미터오류("0000", "param.error", "파라미터 오류 시"),

    //CRUD
    조회("1001", "read.success", "조회"),
    수정("1002", "update.success", "수정"),
    생성("1003", "create.success", "생성"),
    삭제("1004", "delete.success", "삭제"),

    //로그인
    로그인성공("0", "login.success", "로그인 성공 시"),
    로그인실패_회원가입필요("1", "login.join.need", "회원가입 필요 시"),
    로그인실패_패스워드틀림("-1", "login.fail", "로그인 실패 시 - 아이디/비밀번호가 틀릴 시"),
    로그인실패_파라메터이상("-2", "param.error", "로그인 실패 시 - 파라메터이상"),


    //회원가입
    회원가입안됨("1","member.join.not", "회원가입 안된 상태 시"),
    회원가입성공("0", "member.join.success", "회원가입 성공 시"),
    회원가입실패_중복가입("-1", "member.join.already", "이미 회원가입된 상태 시"),
    회원가입실패_닉네임중복("-2", "member.join.nick.duplicate", "닉네임 중복 시"),
    회원가입실패_파라메터오류("-3", "param.error", "파라메터 오류 시"),
    회원가입오류("4005", "member.join.error", "회원가입 오류 시"),

    //닉네임중복체크
    닉네임중복("0", "member.join.nick.duplicate", "닉네임 중복 시"),
    닉네임사용가능("1", "member.join.nick.possible", "닉네임 사용가능 시"),
    닉네임_파라메터오류("10001", "param.error", "닉네임 파라메터 오류 시"),

    //비밀번호변경
    비밀번호변경실패_회원아님("0", "member.change.password.fail", "비밀번호 변경 실패 시"),
    비밀번호변경성공("1", "member.change.password.success", "비밀번호 변경 성공 시"),

    //프로필편집
    프로필편집성공("0", "member.edit.profile.success", "프로필 편집 성공 시"),
    프로필편집실패_회원아님("-1", "member.edit.profile.fail.notUser", "프로필 편집 실패 - 회원이 아닌경우"),
    프로필편집실패_닉네임중복("-2", "member.edit.profile.fail.duplicateNickName", "프로필 편집 실패 - 닉네임이 중복된 경우"),

    //회원팬등록
    팬등록성공("0", "member.insert.fanstar.success", "회원 팬 등록 성공 시"),
    팬등록_회원아님("-1", "member.insert.fan.no", "회원이 아닐 시"),
    팬등록_스타회원번호이상("-2", "member.insert.star.number.strange", "스타회원번호 이상 시"),
    팬등록_이미팬등록됨("-3", "member.insert.fan.already", "이미 팬 등록되어있을 시"),
    팬등록실패("-4", "member.insert.fanstar.fail", "회원 팬 등록 실패 시 "),

    //회원팬해제
    팬해제성공("0", "member.delete.fanstar.success", "팬 해제 성공 시"),
    팬해제_회원아님("-1", "member.delete.fan.no", "회원이 아닐 시"),
    팬해제_스타회원번호이상("-2", "member.delete.star.number.strange", "스타회원번호 이상 시"),
    팬해제_팬아님("-3", "member.delete.fan.not", "팬이 아닐 시"),
    팬해제실패("-4", "member.delete.fanstar.fail", "팬 해제 실패 시"),

    //회원 마이페이지 팬보드 댓글 달기
    팬보드_댓글달기성공("0", "fanboard.add.suceess", "댓글달기 성공 시"),
    팬보드_댓글달기실패_스타회원번호_회원아님("-1", "fanboard.add.fail.StarNo.notMember", "스타 회원번호가 회원이 아님"),
    팬보드_댓글달기실패_작성자회원번호_회원아님("-2", "fanboard.add.fail.writerNo.notMember", "작성자 회원번호가 회원이 아님"),
    팬보드_댓글달기실패_잘못된댓글그룹번호("-3", "fanboard.add.fail.wrong.groupNo", "댓글 그룹번호가 잘못된 번호"),
    팬보드_댓글달기실패_depth값_오류("-4", "fanboard.add.fail.wrong.depth", " depth 값 잘못됨"),

    //회원 마이페이지 팬보드 댓글 리스트
    팬보드조회성공("00000", "fanboard.list.comment.success", "팬보드 정보 조회 시"),
    팬보드_댓글없음("0", "fanboard.list.comment.no", "댓글이 없음"),
    팬보드_요청회원번호_회원아님("-1", "fanboard.list.requestNo.notMember", "요청 회원번호가 회원이 아님"),
    팬보드_스타회원번호_회원아님("-2", "fanboard.list.starNo.notMember","스타 회원번호가 회원이 아님"),

    //회원 마이페이지 팬보드 댓글 삭제
    팬보드_댓글삭제성공("0", "fanboard.delete.comment.success", "댓글 삭제 성공"),
    팬보드_댓글삭제실패_스타회원번호_회원아님("-1", "fanboard.delete.starNo.notMember", "스타 회원번호가 회원이 아님"),
    팬보드_댓글삭제실패_삭제자회원번호_회원아님("-2", "fanboard.delete.writerNo.notMember", "삭제자 회원번호가 회원이 아님"),
    팬보드_댓글삭제실패_댓글인덱스번호_잘못된번호("-3", "fanboard.delete.wrong.indexNo", "댓글 인덱스번호가 잘못된 번호"),
    팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름("-4", "fanboard.delete.notSame.IndexNoStarNo", "요청 인덱스번호의 스타 회원번호가 다름"),
    팬보드_댓글삭제실패_이미삭제됨("-5", "fanboard.delete.already.delete", "이미 삭제됨"),
    팬보드_댓글삭제실패_삭제권한없음("-6", "fanboard.delete.authorization.not", "삭제 권한이 없음"),

    //회원 마이페이지 팬보드 대댓글 보기
    팬보드_대댓글조회성공("00001", "fanboard.reply.comment.success", "팬보드 대댓글 조회 시"),
    팬보드_대댓글조회실패_대댓글없음("0", "fanboard.reply.comment.no", "대댓글 없음"),
    팬보드_대댓글조회실패_요청회원번호_회원아님("-1", "fanboard.reply.requestNo.notMember", "요청 회원번호가 회원이 아님"),
    팬보드_대댓글조회실패_스타회원번호_회원아님("-2", "fanboard.reply.starNo.notMember", "스타 회원번호가 회원이 아님"),

    //회원정보보기    profileService
    회원정보_성공("0", "member.info.view.success", "회원 팬 등록 성공 시"),
    회원정보_회원아님("-1", "member.no", "회원이 아닐 시"),
    회원정보_대상아님("-2", "member.info.view.fail", "회원 팬 등록 실패 시 "),

    //회원정보조회    mypage
    회원정보조회성공("0", "mypage.info.view.success", "회원정보보기 성공 시"),
    회원정보조회_회원아님("-1", "mypage.no", "회원이 아닐 시"),
    회원정보조회_대상회원아님("-2", "mypage.this.no", "대상회원이 아닐 시"),
    회원정보조회_실패("-3", "mypage.info.view.fail", "회원정보보기 실패 시"),

    //회원 방송방 기본설정 조회
    방송방기본설정조회_성공("0", "mypage.broad.basic.suceess", "방송방 기본설정 조회 성공 시"),
    방송방기본설정조회_회원아님("-1", "mypage.broad.basic.no", "회원이 아닐 시"),

    //회원 방송방 기본설정 수정
    방송방기본설정수정_성공("0", "mypage.broad.basic.edit.suceess", "방송방 기본설정 수정 성공 시"),
    방송방기본설정수정_회원아님("-1", "mypage.broad.basic.edit.no", "회원이 아닐 시"),

    //회원 신고하기
    회원신고성공("0", "mypage.member.report.add.suceess", "회원 신고 성공 시"),
    회원신고_요청회원번호_정상아님("-1", "mypage.member.report.add.mem.no", "요청 회원번호 정상 아닐 시"),
    회원신고_신고회원번호_정상아님("-2", "mypage.member.report.add.reported.no", "신고 회원번호 정상 아닐 시"),
    회원신고_이미_신고상태("-3", "mypage.member.reported", "이미 신고 상태 시"),

    //회원 차단하기
    회원차단성공("0", "mypage.member.block.suceess", "회원 차단 성공 시"),
    회원차단_요청회원번호_정상아님("-1", "mypage.member.block.mem.no", "요청 회원번호 정상 아닐 시"),
    회원차단_신고회원번호_정상아님("-2", "mypage.member.block.blocked.mem.no", "차단 회원번호 정상 아닐 시"),
    회원차단_이미_신고상태("-3", "mypage.member.block.blocked", "이미 차단 상태 시"),

    //회원 차단해제하기
    회원차단해제성공("0", "mypage.member.block.del.suceess", "회원 차단 해제 성공 시"),
    회원차단해제_요청회원번호_정상아님("-1", "mypage.member.block.del.mem.no", "요청 회원번호 정상 아닐 시"),
    회원차단해제_신고회원번호_정상아님("-2", "mypage.member.block.blocked.del.mem.no", "차단 회원번호 정상 아닐 시"),
    회원차단안된상태("-3", "mypage.member.block.no", "차단 안된 상태 시"),

    //회원 알림설정 조회하기
    알림설정조회성공("0", "mypage.member.notify.sel.suceess", "알림 설정조회 성공 시"),
    알림설정_회원아님("-3", "mypage.member.notify.no", "알림설정 회원아닐 시"),
    알림설정수정성공("0", "mypage.member.notify.edit.suceess", "알림 설정수정 성공 시"),

    //회원 방송방 기본설정 수정
    방송방기본설정_수정성공("0", "myprofile.broad.basic.edit.suceess", "방송방 기본설정 수정 성공 시"),
    방송방기본설정_회원아님("-1", "myprofile.broad.basic.edit.no", "회원이 아닐 시"),

    //방송생성
    방송생성("0", "broadcast.room.start", "방송 생성 시"),
    방송생성_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송중인방존재("-2", "broadcast.room.existence", "방송중인 방이 있을 시"),
    방생성실패("-3", "broadcast.room.fail", "방송 생성 실패 시"),

    //방송방 참가를 위한 스트림아이디, 토큰받아오기
    방송참여토큰발급("0", "broadcast.token.success", "토큰 발급(방참가) 성공 시"),
    방송참여토큰_해당방이없음("-1", "broadcast.token.room.no", "해당 방이 없을 시"),
    방송참여토큰_방장이없음("-2", "broadcast.token.bj.no", "BJ가 없을 시"),
    방송참여토큰발급_실패("-3", "broadcast.token.fail", "토큰 발급 실패 시"),

    //방송참여
    방송참여성공("0", "broadcast.room.join.success", "방송 참가 성공 시"),
    방송참여_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송참여_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송참여_종료된방송("-3", "broadcast.room.end", "종료된 방송일 시"),
    방송참여_이미참가("-4", "broadcast.room.join.already", "이미 참가 되어있을 시"),
    방송참여_입장제한("-5", "broadcast.room.join.no", "입장제한 시"),
    방송참여_나이제한("-6", "broadcast.room.join.no.age", "나이제한 시"),
    방참가실패("-7", "broadcast.room.join.fail", "방송 참가 실패 시"),

    //방송나가기
    방송나가기("0", "broadcast.room.out", "방송 나가기 시"),
    방송나가기_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송나가기_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송나가기_종료된방송("-3", "broadcast.room.end", "종료된 방송일 시"),
    방송나가기_방참가자아님("-4", "broadcast.room.join.member.no", "방송 참가자 아닐 시"),
    방송나가기실패("-5", "broadcast.out.fail", "방송 나가기 실패 시"),

    //방송정보수정
    방송정보수정성공("0", "broadcast.room.edit.success", "방송정보 수정 성공 시"),
    방송정보수정_회원아님("-1", "broadcast.room.edit.member.no", "회원이 아닐 시"),
    방송정보수정_해당방이없음("-2", "broadcast.room.no", "해당 방이 없을 시"),
    방송정보수정_해당방에없는회원번호("-3", "broadcast.room.member.number.no", "해당 방에 없는 회원번호일 시"),
    방송정보수정_수정권이없는회원("-4", "broadcast.room.member.edit.no", "수정권한이 없는 회원일 시"),
    방송정보수정실패("-5", "broadcast.room.edit.fail", "방송정보 수정 실패 시"),

    //방송리스트
    방송리스트조회("00000", "broadcast.room.list.select.success", "방송리스트 정보 조회 성공 시"),
    방송리스트없음("0", "broadcast.room.list.no", "방송리스트가 없을 시"),
    방송리스트_회원아님("-1", "broadcast.room.list.member.number.no", "회원번호가 아닐 시"),
    방송리스트조회_실패("-2", "broadcast.room.list.select.fail", "방송리스트 정보 조회 실패 시"),

    //방송리스트
    방송참여자리스트_조회("00000", "broadcast.room.list.member.select.success", "방송참여자리스트 정보 조회 시"),
    방송참여자리스트없음("0", "broadcast.room.member.list.no", "방송참여자 리스트가 없을 시"),
    방송참여자리스트_회원아님("-1", "broadcast.room.list.member.number", "회원번호가 아닐 시"),
    방송참여자리스트조회_실패("-2", "broadcast.room.list.member.select.fail", "방송참여자 리스트 조회 실패 시"),

    //방송종료
    방송종료("2004", "broadcast.end", "방송 종료 시"),

    //방송좋아요
    좋아요("0", "broadcast.like.success", "좋아요 성공 시"),
    좋아요_회원아님("-1", "broadcast.like.member.no", "회원이 아닐 시"),
    좋아요_해당방송없음("-2", "broadcast.like.room.no", "해당 방송이 없을 시"),
    좋아요_방송참가자아님("-3", "broadcast.like.room.in.no", "방송 참가자가 아닐 시"),
    좋아요_이미했음("-4", "broadcast.like.already", "좋아요 이미 했을 시"),
    좋아요_실패("-5", "broadcast.like.fail", "좋아요 실패 시"),

    //방송방 게스트지정
    게스트지정("0", "broadcast.guest.add.success", "게스트 지정 성공 시"),
    게스트지정_회원아님("-1", "broadcast.guest.add.member.no", "회원이 아닐 시"),
    게스트지정_해당방이없음("-2", "broadcast.guest.add.room.no", "해당 방송이 없을 시"),
    게스트지정_방이종료되었음("-3", "broadcast.guest.add.room.end", "방이 종료되어 있을 시"),
    게스트지정_방소속_회원아님("-4", "broadcast.guest.add.room.join.no", "방에 소속된 회원이 아닐 시"),
    게스트지정_방장아님("-5", "broadcast.guest.add.bj.no", "방장이 아닐 시"),
    게스트지정_방소속_회원아이디아님("-6", "broadcast.guest.add.room.join.id.no", "방에 소속된 회원아이디가 아닐 시"),
    게스트지정_불가("-7", "broadcast.guest.add.no", "게스트 지정(이미 게스트 또는 방장 권한) 안될 시 "),
    게스트지정_실패("-8", "broadcast.guest.add.fail", "게스트 지정 실패 시"),

    //방송방 게스트 취소
    게스트취소("0", "broadcast.guest.cancel.success", "게스트 취소 성공 시"),
    게스트취소_회원아님("-1", "broadcast.guest.cancel.member.no", "회원이 아닐 시"),
    게스트취소_해당방이없음("-2", "broadcast.guest.cancel.room.no", "해당 방송이 없을 시"),
    게스트취소_방이종료되었음("-3", "broadcast.guest.cancel.room.end", "방이 종료되어 있을 시"),
    게스트취소_방소속_회원아님("-4", "broadcast.guest.cancel.room.join.no", "방에 소속된 회원이 아닐 시"),
    게스트취소_방장아님("-5", "broadcast.guest.cancel.bj.no", "방장이 아닐 시"),
    게스트취소_방소속_회원아이디아님("-6", "broadcast.guest.cancel.room.join.id.no", "방에 소속된 회원아이디가 아닐 시"),
    게스트취소_불가("-7", "broadcast.guest.cancel.no", "게스트가 아닐 시 "),
    게스트취소_실패("-8", "broadcast.guest.cancel.fail", "게스트 취소 실패 시"),

    //유저
    매니저지정("3001", "broadcast.user.manager.add", "매니저 지정 시"),
    게스트초대("3003", "broadcast.user.guest.invite", "게스트 초대 시"),
    게스트초대수락("3005", "broadcast.user.guest.join", "게스트 초대 수락 시"),
    게스트신청("3006", "broadcast.user.guest.apply", "게스트 신청 시"),

    ;

    final private String SUCCESS_RESULT = "success";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    Status(String messageCode, String messageKey, String desc){
        this.result = SUCCESS_RESULT;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
