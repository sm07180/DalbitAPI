package com.dalbit.common.code;

import com.dalbit.common.vo.JsonOutputVo;
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
    벨리데이션체크("C007", "validation.error", "벨리데이션체크 오류 시"),
    부적절한문자열("C007", "string.error", "부적합한 기호 및 문자열 포함 시"),

    //휴대폰 sms 인증
    인증번호요청("0", "sms.number.request.success", "인증번호 요청 성공 시"),
    인증확인("1", "sms.number.check.success", "인증확인 성공 시"),
    인증시간초과("-1", "sms.check.timeout", "인증시간 초과 시"),
    인증번호불일치("-2", "sms.check.number.error", "인증 번호 일치하지 않을 시"),
    인증CMID불일치("-3", "sms.check.cmid.error", "인증 CMID 일치하지 않을 시"),
    인증번호요청_회원아님("-4","sms.number.request.member.no","인증번호 요청"),
    인증번호요청_유효하지않은번호("-5","sms.phone.number.error.no","유효하지않은 휴대폰 번호일 시"),
    인증번호요청실패("C006","sms.number.request.fail","인증번호 요청 실패 시"),
    인증실패("C006", "sms.check.fail", "인증 실패 시"),

    //본인인증요청
    본인인증요청("0", "self.auth.request.success", "본인인증 요청 성공 시"),
    본인인증요청실패("C006", "self.auth.request.fail", "본인인증 요청 실패 시"),

    //본인인증확인
    본인인증확인("0", "self.auth.response.success", "본인인증 확인 성공 시"),
    본인인증검증_비정상접근("-1", "self.auth.response.check.error", "본인인증 비정상적인 접근 시"),
    본인인증실패("C006", "self.auth.response.fail", "본인인증 확인 실패 시"),

    //본인인증(DB저장)
    본인인증성공("0", "member.certification.success", "본인인증 성공 시"),
    본인인증_회원아님("-1", "member.certification.not.member", "요청회원번호 회원이 아닐 시"),
    본인인증_중복("-2", "member.certification.already", "본인인증 이미 되어있는 회원일 시"),
    본인인증저장실패("C006", "member.certification.save.fail", "본인인증 저장 실패 시"),

    //본인인증 여부체크
    본인인증여부_확인("1", "member.certification.check.success", "본인인증 했을 시"),
    본인인증여부_안됨("0", "member.certification.check.no", "본인인증 하지 않았을 시"),
    본인인증여부_회원아님("-1", "member.certification.check.not.member", "요청회원번호 회원이 아닐 시"),
    본인인증여부_실패("C006", "member.certification.check.fail", "본인인증 체크 실패 시"),

    //로그인
    로그인성공("0", "login.success", "로그인 성공 시"),
    관리자로그인성공("0", "admin.login.success", "관리자 아이디로 로그인 성공 시"),
    로그인실패_회원가입필요("1", "login.join.need", "회원가입 필요 시"),
    로그인실패_패스워드틀림("-1", "login.fail", "로그인 실패 시 - 아이디/비밀번호가 틀릴 시"),
    로그인실패_파라메터이상("-2", "login.param.error", "로그인 실패 시 - 파라메터이상"),
    로그인실패_블럭상태("-3", "login.block", "로그인 실패 시 - 블럭상태"),
    로그인실패_탈퇴("-4", "login.kick.out", "로그인 실패 시 - 탈퇴"),
    로그인실패_영구정지("-5", "login.permanent.stop", "로그인 실패 시 - 영구정지"),
    로그인오류("C006", "login.error", "로그인 오류 시"),

    //로그아웃
    로그아웃성공("0", "logout.success", "로그아웃 성공 시"),
    로그아웃실패_진행중인방송("0", "logout.mybroadcast.ing", "로그아웃을 시도했지만 방송을 진행하고 있을 때"),

    //진행중인방송방 체크
    방송중인DJ체크_방송중("1","dj.broadcast.onair", "DJ가 방송중일 시"),
    방송중인DJ체크_방송중아님("0","dj.broadcast.offair", "DJ가 방송중이 아닐 시"),
    방송중인DJ체크_잘못된회원번호("-1","dj.notMember", "DJ가 방송중이 아닐 시"),

    //회원가입
    회원가입안됨("1","member.join.not", "회원가입 안된 상태 시"),
    회원가입성공("0", "member.join.success", "회원가입 성공 시"),
    회원가입실패_중복가입("-1", "member.join.already", "이미 회원가입된 상태 시"),
    회원가입실패_닉네임중복("-2", "member.join.nick.duplicate", "닉네임 중복 시"),
    회원가입실패_파라메터오류("-3", "param.error", "파라메터 오류 시"),
    회원가입실패_탈퇴회원("-4", "member.join.kick.out.member", "탈퇴 회원이 재 가입시"),
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
    회원정보보기_성공("0", "member.info.view.success", "회원정보 보기 성공 시"),
    회원정보보기_회원아님("-1", "member.no", "요청회원번호 회원 아닐 시"),
    회원정보보기_대상아님("-2", "member.this.no", "대상회원번호 회원 아닐 시"),
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
    팬보드_댓글없음("0", "fanboard.list.comment.no.success", "댓글이 없음"),
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
    팬보드_댓글삭제오류("C006", "fanboard.delete.fail", "팬보드 댓글 삭제 실패"),

    //회원 마이페이지 팬보드 댓글 수정
    팬보드_댓글수정성공("0", "fanboard.edit.success", "댓글수정 성공 시"),
    팬보드_댓글수정실패_스타회원번호_회원아님("-1", "fanboard.edit.fail.StarNo.notMember", "스타 회원번호가 회원이 아닐 시"),
    팬보드_댓글수정실패_수정자회원번호_회원아님("-2", "fanboard.edit.fail.writerNo.notMember", "수정자 회원번호가 회원이 아닐 시"),
    팬보드_댓글수정실패_잘못된댓글인덱스번호("-3", "fanboard.edit.fail.wrong.board.idx", "댓글 인덱스가 잘못된 번호일 시"),
    팬보드_댓글수정실패_댓글인덱스번호_스타회원번호다름("-4", "fanboard.edit.fail.board.idx.not.star.member.number", "댓글 인덱스번호와 스타회원번호가 다를 시"),
    팬보드_댓글수정실패_삭제댓글_수정불가("-5", "fanboard.edit.fail.delete.reply", "이미 삭제되어 수정 불가 시" ),
    팬보드_댓글수정실패_수정권한없음("-6", "fanboard.edit.fail.authorization.not", "수정 권한이 없을 시"),
    팬보드_댓글수정실패("C006", "fanboard.edit.fail", "댓글 수정 실패 시"),

    //회원 마이페이지 팬보드 대댓글 보기
    팬보드_대댓글조회성공("C001", "fanboard.reply.comment.success", "팬보드 대댓글 조회 시"),
    팬보드_대댓글조회실패_대댓글없음("0", "fanboard.reply.comment.no.success", "대댓글 없을 시"),
    팬보드_대댓글조회실패_요청회원번호_회원아님("-1", "fanboard.reply.requestNo.notMember", "요청 회원번호가 회원이 아닐 시"),
    팬보드_대댓글조회실패_스타회원번호_회원아님("-2", "fanboard.reply.starNo.notMember", "스타 회원번호가 회원이 아닐 시"),
    팬보드_대댓글조회오류("C006", "fanboard.reply.error", "팬보드 대댓글 보기 실패 시"),

    //회원 달 선물하기
    달선물_성공("0", "mypage.member.gift.dal.success", "달 선물 성공 시"),
    달선물_요청회원번호_회원아님("-1", "mypage.member.gift.dal.member.no", "요청회원번호가 회원이 아닐 시"),
    달선물_받는회원번호_회원아님("-2", "mypage.member.gift.dal.object.member.no", "받는회원번호가 회원이 아닐 시"),
    달선물_달개수_비정상("-3", "mypage.member.gift.dal.count.error", "선물하는 달 개수가 비정상일 시"),
    달선물_달개수_부족("-4", "mypage.member.gift.dal.limit", "달 개수가 부족할 시"),
    달선물_실패("C006", "mypage.member.gift.dal.fail", "달 선물 실패 시"),

    //방송생성
    방송생성("0", "broadcast.room.start.success", "방송 생성 시"),
    방송생성_회원아님("-1", "broadcast.room.member.no", "회원이 아닐 시"),
    방송중인방존재("-2", "broadcast.room.existence", "방송중인 방이 있을 시"),
    방송생성_deviceUuid비정상("-3", "broadcast.room.deviceUuid.error", "deviceUuid 비정상일 시"),
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
    방송방사연등록_회원아님("-1", "broadcast.room.story.add.no", "방송방 사연 등록 회원이 아닐 시"),
    방송방사연등록_해당방이없음("-2", "broadcast.room.story.add.room.no", "방송방 사연등록 해당 방이 없을 시"),
    방송방사연등록_방참가자가아님("-3", "broadcast.room.story.add.member.no", "방송방 사연등록 방참가자가 아닐 시"),
    방송방사연등록_10분에한번등록가능("-4", "broadcast.room.story.add.time", "방송방 사연등록 10분에 한번 씩 등록 가능"),
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
    선물하기_아이템번호없음("-7", "broadcast.room.gift.item.number.no", "선택된 아이템이 없을 시"),
    선물하기_달부족("-8", "broadcast.room.gift.limit.dal", "달이 부족할 시"),
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
    매니저지정_성공("0", "broadcast.room.manager.add.success", "매니저지정 성공 시"),
    매니저지정_회원아님("-1", "broadcast.room.manager.add.member.no", "회원이 아닐 시"),
    매니저지정_해당방이없음("-2", "broadcast.room.manager.add.no.room", "해당 방 없을 시"),
    매니저지정_방이종료되었음("-3", "broadcast.room.manager.add.room.end", "방이 종료되었을 시"),
    매니저지정_요청회원_방소속아님("-4", "broadcast.room.manager.add.member.join.no", "요청회원이 방 소속이 아닐 시"),
    매니저지정_요청회원_방장아님("-5", "broadcast.room.manager.add.member.not.bj", "요청회원이 방장이 아닐 시"),
    매니저지정_대상회원아이디_방소속아님("-6", "broadcast.room.manager.add.member.not.join.id", "대상 회원아이디가 방 소속이 아닐 시"),
    매니저지정_불가("-7", "broadcast.room.manager.add.error", "매니저지정 불가 시(이미 매니저거나 방장 권한)"),
    매니저지정_인원제한("-8", "broadcast.room.manager.add.limit", "매니저 수 제한 시"),
    매니저지정_실패("C006", "broadcast.room.manager.add.fail", "매니저지정 실패 시"),

    //방송방 매니저취소
    매니저취소_성공("0", "broadcast.room.manager.del.success", "매니저취소 성공 시"),
    매니저취소_회원아님("-1", "broadcast.room.manager.del.member.no", "회원이 아닐 시"),
    매니저취소_해당방이없음("-2", "broadcast.room.manager.del.no.room", "해당 방 없을 시"),
    매니저취소_방이종료되었음("-3", "broadcast.room.manager.del.room.end", "방이 종료되었을 시"),
    매니저취소_요청회원_방소속아님("-4", "broadcast.room.manager.del.member.join.no", "요청회원이 방 소속이 아닐 시"),
    매니저취소_요청회원_방장아님("-5", "broadcast.room.manager.del.member.not.bj", "요청회원이 방장이 아닐 시"),
    매니저취소_대상회원아이디_방소속아님("-6", "broadcast.room.manager.del.member.not.join.id", "대상 회원아이디가 방 소속이 아닐 시"),
    매니저취소_매니저아님("-7", "broadcast.room.manager.del.no", "매니저 아닐 시"),
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
    방송방회원정보조회_대상회원_방에없음("-3", "broadcast.room.member.info.join.no", "대상 회원이 방에 없을 시"),
    방송방회원정보조회_실패("C006", "broadcast.room.member.info.select.fail", "방송방 회원정보 조회 실패 시"),

    //회원 알림 내용 조회
    회원알림내용조회_성공("C001", "mypage.member.notification.select.success", "회원 알림내용 조회 성공 시"),
    회원알림내용조회_알림없음("0", "mypage.member.notification.select.no.success", "알림 없을 시"),
    회원알림내용조회_회원아님("-1", "mypage.member.notification.select.not.member", "회원이 아닐 시"),
    회원알림내용조회_실패("C006", "mypage.member.notification.select.fail", "회원 알림내용 조회 실패 시"),

    //회원 닉네임 검색
    회원닉네임검색_성공("C001", "member.nick.search.success", "회원 닉네임 검색 성공 시"),
    회원닉네임검색_결과없음("0", "member.nick.search.no.success", "결과 없을 시"),
    회원닉네임검색_실패("C006", "member.nick.search.fail", "회원 닉네임 검색 실패 시"),

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

    //팬 랭킹 조회
    팬랭킹조회_성공("C001", "member.fan.ranking.select.success", "팬 랭킹 조회 성공 시"),
    팬랭킹조회_팬없음("0", "member.fan.ranking.no.fan.success", "팬 없을 시"),
    팬랭킹조회_요청회원_회원아님("-1", "member.fan.ranking.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    팬랭킹조회_대상회원_회원아님("-2", "member.fan.ranking.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    팬랭킹조회_실패("C006", "member.fan.ranking.select.fail", "팬 랭킹 조회 실패 시"),

    //마이페이지 공지사항 등록
    공지등록_성공("0", "mypage.notice.create.success", "마이페이지 공지등록 성공 시"),
    공지등록_요청회원번호_회원아님("-1", "mypage.notice.create.member.number.error", "요청회원번호가 회원 아닐 시"),
    공지등록_대상회원번호_회원아님("-2", "mypage.notice.create.object.member.number.error", "대상회원번호가 회원 아닐 시"),
    공지등록_권한없음("-3", "mypage.notice.create.authorization.not", "공지등록 권한 없을 시"),
    공지등록_실패("C006", "mypage.notice.create.fail", "마이페이지 공지등록 실패 시"),

    //마이페이지 공지사항 수정
    공지수정_성공("0", "mypage.notice.edit.success", "마이페이지 공지수정 성공 시"),
    공지수정_요청회원번호_회원아님("-1", "mypage.notice.edit.member.number.error", "요청회원번호가 회원 아닐 시"),
    공지수정_대상회원번호_회원아님("-2", "mypage.notice.edit.object.member.number.error", "대상회원번호가 회원 아닐 시"),
    공지수정_권한없음("-3", "mypage.notice.edit.authorization.not", "공지수정 권한 없을 시"),
    공지수정_잘못된공지번호("-4", "mypage.notice.edit.number.error", "공지번호가 잘못된 번호일 시"),
    공지수정_실패("C006", "mypage.notice.edit.fail", "마이페이지 공지수정 실패 시"),

    //마이페이지 공지사항 삭제
    공지삭제_성공("0", "mypage.notice.delete.success", "마이페이지 공지삭제 성공 시"),
    공지삭제_요청회원번호_회원아님("-1", "mypage.notice.delete.member.number.error", "요청회원번호가 회원 아닐 시"),
    공지삭제_대상회원번호_회원아님("-2", "mypage.notice.delete.object.member.number.error", "대상회원번호가 회원 아닐 시"),
    공지삭제_권한없음("-3", "mypage.notice.delete.authorization.not", "공지삭제 권한 없을 시"),
    공지삭제_잘못된공지번호("-4", "mypage.notice.delete.number.error", "공지번호가 잘못된 번호일 시"),
    공지삭제_실패("C006", "mypage.notice.delete.fail", "마이페이지 공지삭제 실패 시"),

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
    블랙리스트등록_이미블랙등록("-4", "mypage.broadcast.blacklist.add.already.add", "이미 블랙리스트 등록 되어있을 시"),
    블랙리스트등록_실패("C006", "mypage.broadcast.blacklist.add.fail", "블랙리스트 등록 실패 시"),

    //마이페이지 방송설정 블랙리스트 해제
    블랙리스트해제_성공("0", "mypage.broadcast.blacklist.delete.success", "블랙리스트 해제 성공 시"),
    블랙리스트해제_요청회원번호_회원아님("-1", "mypage.broadcast.blacklist.delete.member.number.error", "요청회원번호가 회원 아닐 시"),
    블랙리스트해제_블랙회원번호_회원아님("-2", "mypage.broadcast.blacklist.delete.number.error", "블랙리스트 회원번호가 회원 아닐 시"),
    블랙리스트해제_블랙회원없음("-3", "mypage.broadcast.blacklist.delete.add.blacklist.no", "등록된 블랙리스트 회원이 없을 시"),
    블랙리스트해제_실패("C006", "mypage.broadcast.blacklist.delete.fail", "블랙리스트 해제 실패 시"),

    // 회원 레벨 업 확인
    레벨업확인_성공("0", "member.levelup.check.success", "레벨 업 확인 성공 시"),
    레벨업확인_요청회원번호_회원아님("-1", "member.levelup.check.member.number.error", "요청회원번호가 회원 아닐 시"),
    레벨업확인_레벨업_없음("-2", "member.levelup.check.no", "레벨업 없을 시"),
    레벨업확인_실패("C006", "member.levelup.check.fail", "레벨 업 확인 실패 시"),

    //고객센터 공지사항 목록 조회
    고객센터_공지사항조회_성공("C001", "customer.center.notice.select.success", "고객센터 공지사항 조회 성공 시"),
    고객센터_공지사항조회_없음("0", "customer.center.notice.no.success", "고객센터 공지사항 목록 없을 시"),
    고객센터_공지사항조회_실패("C006", "customer.center.notice.select.fail", "고객센터 공지사항 조회 실패 시"),

    //고객센터 공지사항 내용 조회
    고객센터_공지사항내용조회_성공("0", "customer.center.notice.detail.select.success", "고객센터 공지사항 내용조회 성공 시"),
    고객센터_공지사항내용조회_없음("-1", "customer.center.notice.detail.no", "고객센터 공지사항 내용 없을 시"),
    고객센터_공지사항내용조회_실패("C006", "customer.center.notice.detail.select.fail", "고객센터 공지사항 내용조회 실패 시"),

    //고객센터 FAQ 목록 조회
    고객센터_FAQ조회_성공("C001", "customer.center.faq.select.success", "고객센터 FAQ 조회 성공 시"),
    고객센터_FAQ조회_없음("0", "customer.center.faq.no.success", "고객센터 FAQ 목록 없을 시"),
    고객센터_FAQ조회_실패("C006", "customer.center.faq.select.fail", "고객센터 FAQ 조회 실패 시"),

    //고객센터 FAQ 내용 조회
    고객센터_FAQ내용조회_성공("0", "customer.center.faq.detail.select.success", "고객센터 FAQ 내용조회 성공 시"),
    고객센터_FAQ내용조회_없음("-1", "customer.center.faq.detail.no", "고객센터 FAQ 내용 없을 시"),
    고객센터_FAQ내용조회_실패("C006", "customer.center.faq.detail.select.fail", "고객센터 FAQ 내용조회 실패 시"),

    //고객센터 1:1문의작성 등록
    고객센터_문의작성_성공("0", "customer.center.qna.add.success", "고객센터 1:1문의하기 작성 성공 시"),
    고객센터_문의작성_요청회원번호_회원아님("-1", "customer.center.qna.add.member.number.error", "요청회원번호가 회원 아닐 시"),
    고객센터_문의작성_실패("C006", "customer.center.qna.add.fail", "고객센터 1:1문의하기 작성 실패 시"),

    //고객센터 문의내역 조회
    고객센터_문의내역조회_성공("C001", "customer.center.qna.select.success", "고객센터 문의내역 조회 성공 시"),
    고객센터_문의내역_없음("0", "customer.center.qna.no.success", "고객센터 문의내역 없을 시"),
    고객센터_문의내역조회_실패("C006", "customer.center.qna.select.fail", "고객센터 문의내역 조회 실패 시"),

    //회원탈퇴
    회원탈퇴_성공("0", "member.withdrawal.success", "회원탈퇴 성공 시"),
    회원탈퇴_회원아님("-1", "member.withdrawal.member.number.error", "요청회원번호 회원 아닐 시"),
    회원탈퇴_이미탈퇴("-2", "member.withdrawal.already", "이미 탈퇴한 회원일 시"),
    회원탈퇴_방접속중("-3", "member.withdrawal.playing", "방송방 접속중일 시"),
    회원탈퇴_실패("C006", "member.withdrawal.fail", "회원탈퇴 실패 시"),

    //메인 마이DJ
    메인_마이DJ_조회성공("C001", "main.my.dj.select.success", "마이DJ 조회 성공 시"),
    메인_마이DJ_리스트없음("0", "main.my.dj.select.no.success", "마이DJ 내역 없을 시"),
    메인_마이DJ_요청회원_회원아님("-1", "main.my.dj.select.member.number.error", "요청회원번호가 회원 아닐 시"),
    메인_마이DJ_조회실패("C006", "main.my.dj.select.fail", "마이DJ 조회 실패 시"),

    //메인 추천DJ 리스트
    메인_추천DJ리스트_조회성공("C001", "main.recommand.dj.select.success", "추천 DJ리스트 조회 성공 시"),
    메인_추천DJ리스트_없음("0", "main.recommand.dj.select.no.success", "추천 DJ리스트 없음"),
    메인_추천DJ리스트_조회실패("C006", "main.recommand.dj.select.fail", "추천 DJ리스트 조회 실패 시"),

    //에러 로그 저장
    에러로그저장_성공("0", "error.log.save.success", "에러 로그 저장 성공 시"),
    에러로그저장_실패("C006", "error.log.save.fail", "에러 로그 저장 실패 시"),

    //방송시간 연장
    시간연장성공("0", "broadcast.time.extend.success", "방송시간 연장 성공 시"),
    시간연장_회원아님("-1", "broadcast.time.extend.member.number.error", "회원 번호 아닐 시"),
    시간연장_방번호없음("-2", "broadcast.time.extend.room.number.no", "방번호 없을 시"),
    시간연장_종료된방("-3", "broadcast.time.extend.room.exit", "종료된 방일 시"),
    시간연장_이미한번연장("-4", "broadcast.time.extend.already.once", "이미 한번 연장 했을 시"),
    시간연장_남은시간_5분안됨("-5", "broadcast.time.extend.not.remain.5min", "남은시간이 5분 아닐 시"),
    시간연장실패("C006", "broadcast.time.extend.fail", "방송시간 연장 실패 시"),

    //푸시 발송
    푸시성공("0", "push.add.success", "푸시 성공 시"),
    푸시_회원아님("-1", "push.add.member.number.error", "회원 번호 아닐 시"),
    푸시_디바이스토큰없음("-2", "push.add.device.token.no", "디바이스 토큰 없을 시"),
    푸시실패("C006", "push.add.fail", "푸시 실패 시"),

    //메인 나의스타 리스트
    메인_나의스타_조회성공("C001", "main.star.dj.select.success", "나의스타 조회 성공 시"),
    메인_나의스타_없음("0", "main.star.dj.select.no.success", "나의스타 없음"),
    메인_나의스타_회원아님("-1", "main.star.dj.select.no.member", "회원아님"),
    메인_나의스타_조회실패("C006", "main.star.dj.select.fail", "나의스타 조회 실패 시"),

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

    //팬 조회
    팬조회_성공("C001", "member.fan.list.select.success", "팬 랭킹 조회 성공 시"),
    팬조회_팬없음("0", "member.fan.list.no.fan.success", "팬 없을 시"),
    팬조회_요청회원_회원아님("-1", "member.fan.list.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    팬조회_대상회원_회원아님("-2", "member.fan.list.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    팬조회_실패("C006", "member.fan.list.select.fail", "팬 랭킹 조회 실패 시"),

    //금지어 체크
    닉네임금지("C005", "nick.name.ban.word", "닉네임 금지어 포함 시"),
    방송방생성제목금지("C005", "broadcast.create.title.ban.word", "방송방 생성 제목 금지어 포함 시"),
    방송방생성인사말금지("C005", "broadcast.create.welcome.msg.ban.word", "방송방 생성 인사말 금지어 포함 시"),
    방송방수정제목금지("C005", "broadcast.edit.title.ban.word", "방송방 수정 제목 금지어 포함 시"),
    방송방수정인사말금지("C005", "broadcast.edit.welcome.msg.ban.word", "방송방 수정 인사말 금지어 포함 시"),

    //알림 읽음처리
    알림읽음_성공("0", "member.notification.read.success", "알림 읽음 성공 시"),
    알림읽음_회원아님("-1", "member.notification.read.member.number.error", "요청회원번호 회원 아닐 시"),
    알림읽음_실패("C006", "member.notification.read.fail", "알림 읽음 실패 시"),


    //구글 로그인
    구글로그인_성공("0", "google.login.success", "성공"),
    구글로그인_토큰없음("-1", "google.login.blank.token", "토큰없음"),
    구글로그인_토큰인증실패("-2", "google.login.invalid.token", "인증실패"),
    구글로그인_오류("C006", "google.login.error", "오류"),


    //////////////////////////
    //모바일 관리자
    /////////////////////////
    방송강제종료_성공("0", "force.exit.success", "강제종료 성공"),
    방송강제종료_회원아님("-1", "force.exit.no.member", "강제종료 요청 시 잘못된 회원번호"),
    방송강제종료_존재하지않는방("-2", "force.exit.no.room", "강제종료 시 존재하지 않는 방"),
    방송강제종료_이미종료된방("-3", "force.exit.already.end", "이미 종료된 방을 강제종료 한 경우"),
    방송강제종료_방참가자아님("-4", "force.exit.no.listener", "방 참가자가 아닌경우"),

    관리자메뉴조회_권한없음("-1", "no.auth", "관리자 권한이 없는 경우"),

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
    환전신청_별부족("-12", "exchange.apply.byeol.lack ", "별 부족 시"),
    환전신청_신청제한("-13", "exchange.apply.limit", "환전 신청 횟수 초과 시"),
    환전신청실패("C006", "exchange.apply.fail", "환전 신청 실패 시"),

    //스페셜DJ
    스페셜DJ_신청성공("0", "special.dj.apply.success", "스페셜DJ 신청 성공 시"),
    스페셜DJ_회원아님("-1", "special.dj.apply.no.member", "요청회원번호 회원 아닐 시"),
    스페셜DJ_이미신청("-2", "special.dj.apply.already.member", "이미 신청하였을 경우"),
    스페셜DJ_기간아님("-3", "special.dj.apply.passed.period", "기간이 지났을 경우"),
    스페셜DJ_신청실패("C006", "special.dj.apply.fail", "스페셜DJ 신청 실패 시"),

    //프로필이미지 초기화
    프로필이미지초기화_성공("0", "profile.image.init.success", "프로필이미지 초기화 성공 시"),
    프로필이미지초기화_실패("C006", "profile.image.init.fail", "프로필이미지 초기화 실패 시"),

    //방송방이미지 초기화
    방송방이미지초기화_성공("0", "room.image.init.success", "방송방이미지 초기화 성공 시"),
    방송방이미지초기화_실패("C006", "room.image.init.fail", "방송방이미지 초기화 실패 시"),

    //닉네임 초기화
    닉네임초기화_성공("0", "member.nickname.init.success", "닉네임 초기화 성공 시"),
    닉네임초기화_실패("C006", "member.nickname.init.fail", "닉네임 초기화 실패 시"),

    //방송제목 초기화
    방송제목초기화_성공("0", "broadcast.title.init.success", "방송제목 초기화 성공 시"),
    방송제목초기화_실패("C006", "broadcast.title.init.fail", "방송제목 초기화 실패 시"),


    //랭킹 이벤트 실시간 순위보기
    랭킹이벤트실시간순위리스트조회("C001", "event.ranking.live.list.select.success", "랭킹이벤트실시간순위리스트 정보 조회 성공 시"),
    랭킹이벤트실시간순위리스트없음("0", "event.ranking.live.list.no.success", "랭킹이벤트실시간순위리스트가 없을 시"),
    랭킹이벤트실시간순위리스트_실패("C006", "event.ranking.live.list.select.fail", "랭킹이벤트실시간순위리스트 정보 조회 실패 시"),

    //랭킹 이벤트 결과보기
    랭킹이벤트결과조회("C001", "event.ranking.result.list.select.success", "랭킹이벤트결과 정보 조회 성공 시"),
    랭킹이벤트결과없음("0", "event.ranking.result.list.no.success", "랭킹이벤트결과가 없을 시"),
    랭킹이벤트결과_실패("C006", "event.ranking.result.list.select.fail", "랭킹이벤트결과 정보 조회 실패 시"),

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
