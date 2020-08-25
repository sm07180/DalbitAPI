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
    데이터없음("0", "no.data", "데이터가 없을 시"),
    로그인필요("-99", "need.login", "로그인 필요 시"),

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
    본인인증12세미만("C007", "self.auth.12age.limit", "12세 미만 회원일 시"),
    보호자인증20세미만("C008", "self.auth.20age.limit", "보호자 20세 미만일 시"),

    //본인인증(DB저장)
    본인인증성공("0", "member.certification.success", "본인인증 성공 시"),
    본인인증_회원아님("-1", "member.certification.not.member", "요청회원번호 회원이 아닐 시"),
    본인인증_중복("-2", "member.certification.already", "본인인증 이미 되어있는 회원일 시"),
    본인인증저장실패("C006", "member.certification.save.fail", "본인인증 저장 실패 시"),

    //보호자인증(DB업데이트)
    보호자인증성공("0", "member.certification.parents.success", "보호자인증 성공 시"),
    보호자인증실패("C006", "member.certification.parents.fail", "보호자인증 실패 시"),

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
    로그인실패_청취방존재("-6", "login.connected.room", "로그인 실패 시 - 동일방 접속"),
    로그인실패_운영자차단("-7", "login.admin.block", "로그인 실패 시 - 운영자가 차단한 deviceUuid or IP 인 경우"),
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
    프로필편집실패_닉네임짦음("-3", "member.edit.profile.fail.shorNickname", "프로필 편집 실패 - 공백을 제거한 닉네임이 2자보다 짧은경우"),
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
    회원방송방빠른말수정_명령어번호없음("-2", "mypage.member.shorucut.order.number.no", "명렁어 번호가 없을 시"),
    회원방송방빠른말수정_사용기간만료("-3", "mypage.member.shorucut.edit.use.date.limit", "사용기간 만료 시"),
    회원방송방빠른말수정오류("C006", "mypage.member.shorucut.edit.error", "회원 방송방 빠른말 수정 오류 시"),
    회원방송방빠른말수정_텍스트오류("C007", "mypage.member.shorucut.text.error", "회원 방송방 빠른말 수정 텍스트 자리수 "),

    //회원방송방 빠른말 추가
    회원방송방빠른말추가_성공("0", "mypage.member.shorucut.add.success", "회원 방송방 빠른말 추가 성공 시"),
    회원방송방빠른말추가_회원아님("-1", "mypage.member.shorucut.add.no", "요청번호가 회원이 아닐 시"),
    회원방송방빠른말추가_제한("-2", "mypage.member.shorucut.add.limit", "빠른말 추가 제한(6개) 시"),
    회원방송방빠른말추가_달부족("-3", "mypage.member.shorucut.add.dal.limit", "달 부족 시"),
    회원방송방빠른말추가_오류("C006", "mypage.member.shorucut.add.error", "회원 방송방 빠른말 수정 오류 시"),

    //회원방송방 빠른말 연장
    회원방송방빠른말연장_성공("0", "mypage.member.shorucut.extend.success", "회원 방송방 빠른말 연장 성공 시"),
    회원방송방빠른말연장_회원아님("-1", "mypage.member.shorucut.extend.no", "요청번호가 회원이 아닐 시"),
    회원방송방빠른말연장_불가번호("-2", "mypage.member.shorucut.extend.impossible.number", "연장 불가한 번호 시"),
    회원방송방빠른말연장_번호없음("-3", "mypage.member.shorucut.extend.order.number.no", "명령어 번호가 없을 시"),
    회원방송방빠른말연장_사용중인번호("-4", "mypage.member.shorucut.extend.use.order.number", "이미 사용중인 명령어 번호일 시"),
    회원방송방빠른말연장_달부족("-5", "mypage.member.shorucut.extend.dal.limit", "달 부족 시"),
    회원방송방빠른말연장_오류("C006", "mypage.member.shorucut.extend.error", "회원 방송방 빠른말 연장 오류 시"),

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
    방송참여_다른기기("-10", "broadcast.room.join.another", "다른기기에서 청취 시"),
    방송참여_블랙리스트("-11", "broadcast.room.join.blacklist", "블랙리스트에 등록되어 있을 시"),
    방송참여_비회원IP중복("-12", "broadcast.room.join.ano.ip.duplicate", "비회원 IP 동일방 중복 있을 시"),
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
    방송방사연등록_1분에한번등록가능("-4", "broadcast.room.story.add.time", "방송방 사연등록 1분에 한번 씩 등록 가능"),
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
    매니저지정_관리자("-99", "broadcast.room.manager.add.admin", "운영자 추가시"),
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

    //회원 알림 삭제
    회원알림삭제_성공("0", "mypage.member.notification.delete.success", "회원 알림내용 삭제 성공 시"),
    회원알림내용삭제_회원아님("-1", "mypage.member.notification.delete.not.member", "회원이 아닐 시"),
    회원알림내용삭제_실패("C006", "mypage.member.notification.delete.fail", "회원 알림내용 삭제 실패 시"),

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

    //메인 Level 랭킹
    메인_Level랭킹조회_성공("C001", "main.level.ranking.select.success", "Level 랭킹 조회 성공 시"),
    메인_Level랭킹조회_내역없음("0", "main.level.ranking.no.ranking.success", "랭킹 내역 없을 시"),
    메인_Level랭킹조회_실패("C006", "main.level.ranking.select.fail", "Level 랭킹 조회 실패 시"),

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
    고객센터_문의작성_재문의불가("-2", "customer.center.qna.re.add.error", "재문의 불가 시"),
    고객센터_문의작성_실패("C006", "customer.center.qna.add.fail", "고객센터 1:1문의하기 작성 실패 시"),

    //고객센터 문의내역 조회
    고객센터_문의내역조회_성공("C001", "customer.center.qna.select.success", "고객센터 문의내역 조회 성공 시"),
    고객센터_문의내역_없음("0", "customer.center.qna.no.success", "고객센터 문의내역 없을 시"),
    고객센터_문의내역조회_실패("C006", "customer.center.qna.select.fail", "고객센터 문의내역 조회 실패 시"),

    //1:1 문의 상세 조회
    고객센터_문의상세조회_성공("0", "select.success", "1:1문의 상세조회 성공 시"),
    고객센터_문의상세조회_문의번호없음("-1", "no.data", "1:1문의 상세조회에서 문의 번호가 없을 시"),
    고객센터_문의상세조회_에러("C006", "server.error", "1:1문의 상세조회에서 에러 발생 시"),

    //고객센터 1:1문의삭제
    고객센터_문의삭제_성공("0", "customer.center.qna.delete.success", "고객센터 1:1문의하기 삭제 성공 시"),
    고객센터_문의삭제_요청회원번호_회원아님("-1", "customer.center.qna.delete.member.number.error", "요청회원번호가 회원 아닐 시"),
    고객센터_문의삭제_문의번호없음("-2", "customer.center.qna.delete.number.error", "문의번호가 없을 시"),
    고객센터_문의삭제_실패("C006", "customer.center.qna.delete.fail", "고객센터 1:1문의하기 삭제 실패 시"),

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

    출석체크이벤트_상태조회_실패_회원아님("-1", "event.attendance.check.fail.no.member", "요청 회원번호가 없음"),
    출석체크이벤트_상태조회_실패("C006", "business.error", "비즈니스 에러"),
    출석체크이벤트_상태조회_성공("0", "read.success", "성공"),

    출석체크이벤트_출석_성공("0", "event.attendance.checkIn.success", "성공"),
    출석체크이벤트_출석_실패_회원아님("-1", "event.attendance.checkIn.fail.no.member", "요청 회원번호 없음"),
    출석체크이벤트_출석_실패_이미받음("-2", "event.attendance.checkIn.fail.already", "이미 받음"),
    출석체크이벤트_출석_실패_필요시간부족("-3", "event.attendance.checkIn.fail.short.time", "필요시간부족"),
    출석체크이벤트_출석_실패_보상테이블없음("-4", "event.attendance.checkIn.fail.table.emapty", "보상테이블없음"),
    출석체크이벤트_출석_실패_동일기기중복불가("-5", "event.attendance.checkIn.fail.device.check", "동일기기 중복불가"),
    출석체크이벤트_출석_실패("C006", "business.error", "비즈니스 에러"),

    출석체크이벤트_더줘_성공("0", "event.attendance.checkIn.success", "성공"),
    출석체크이벤트_더줘_실패_회원아님("-1", "event.attendance.checkIn.fail.no.member", "요청 회원번호 없음"),
    출석체크이벤트_더줘_실패_이미받음("-2", "event.attendance.bonus.fail.already", "이미 받음"),
    출석체크이벤트_더줘_실패_대상아님("-3", "event.attendance.bonus.no.auth", "대상아님"),
    출석체크이벤트_더줘_실패("C006", "business.error", "비즈니스 에러"),

    설정_방생성_참여불가상태("S001", "system.config.broadcast.block", "tbl_code_defind -> system_config / 방생성_참여_가능여부 가 Y 일경우"),

    라이징이벤트_실시간순위_조회_성공("C001", "event.rising.live.success", "성공"),
    라이징이벤트_실시간순위_데이터없음("0", "event.rising.live.no.data", "데이터 없음"),

    라이징이벤트_결과_조회_성공("C001", "event.rising.result.success", "성공"),
    라이징이벤트_결과_데이터없음("0", "event.rising.result.no.data", "데이터 없음"),

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
    환전신청_별부족("-12", "exchange.apply.byeol.lack", "별 부족 시"),
    환전신청_신청제한("-13", "exchange.apply.limit", "환전 신청 횟수 초과 시"),
    환전신청_기존신청정보오류("-14", "exchange.apply.already.addfile.error", "기존 신청정보 오류 시"),
    환전신청실패("C006", "exchange.apply.fail", "환전 신청 실패 시"),

    //환전승인건조회
    환전승인조회성공("0", "exchange.approval.list.select.success", "환전 승인 건 조회 성공 시"),
    환전승인조회없음("-1", "exchange.approval.list.select.none", "환전 승인 건 없을 시"),

    //스페셜DJ
    스페셜DJ_신청성공("0", "special.dj.apply.success", "스페셜DJ 신청 성공 시"),
    스페셜DJ_회원아님("-1", "special.dj.apply.no.member.success", "요청회원번호 회원 아닐 시"),
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

    //신고처리
    신고처리_성공("0", "declaration.operate.success", "신고처리 성공 시"),
    신고처리_에러("C006", "declaration.fail.server.error", "신고처리에서 에러 발생 시"),

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

    //1:1문의 처리하기
    일대일문의처리_성공("0", "customer.center.qna.operate.success", "1:1문의 처리 성공 시"),
    일대일문의처리_문의번호없음("-1", "customer.center.qna.operate.no.number", "1:1문의 처리에서 문의 번호가 없을 시"),
    일대일문의처리_이미처리됐음("-2", "customer.center.qna.operate.aleady.operate", "1:1문의 처리에서 이미 처리된 문의일 시"),
    일대일문의처리_에러("C006", "customer.center.qna.operate.server.error", "1:1문의 처리에서 에러 발생 시"),
    일대일문의처리_이미_진행중("3", "customer.center.qna.operate.error", "1:1문의 처리에서 에러 발생 (처리 진행중)"),

    //1:1문의 수정하기
    일대일문의수정_성공("0", "customer.center.qna.update.success", "1:1문의 수정 성공 시"),
    일대일문의수정_문의번호없음("-1", "customer.center.qna.update.no.number", "1:1문의 수정에서 문의 번호가 없을 시"),
    일대일문의수정_이미처리됐음("-2", "customer.center.qna.update.aleady.operate", "1:1문의 수정에서 이미 처리된 문의일 시"),
    일대일문의수정_에러("C006", "customer.center.qna.update.server.error", "1:1문의 수정에서 에러 발생 시"),

    //신고내역 조회
    신고목록조회_데이터없음("0", "declaration.list.no.data", "신고목록 조회 데이터가 없을 시"),
    신고목록조회_성공("C001", "declaration.list.select.success", "신고목록 조회 시"),
    신고목록조회_에러("C006", "declaration.list.server.error", "신고목록 조회에서 에러 발생 시"),

    //신고내역 상세조회
    신고상세조회_공지번호없음("-1", "declaration.detail.no.data", "신고목록 조회 데이터가 없을 시"),
    신고상세조회_성공("0", "declaration.detail.select.success", "신고목록 상세조회 시"),
    신고상세조회_에러("C006", "declaration.detail.server.error", "신고상세 조회에서 에러 발생 시"),

    //메시지 사용 클릭
    메시지클릭업데이트_성공("0", "mypage.member.msg.click.update.success", "메시지 클릭 업데이트 성공 시"),
    메시지클릭업데이트_오류("C006", "mypage.member.msg.click.update.error", "메시지 클릭 업데이트 오류 시"),

    //팬 메모 조회
    팬메모조회_성공("0", "member.fan.memo.select.success", "팬 메모조회 성공 시"),
    팬메모조회_요청회원_회원아님("-1", "member.fan.memo.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    팬메모조회_대상회원_회원아님("-2", "member.fan.memo.select.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    팬메모조회_대상회원_팬아님("-3", "member.fan.memo.select.object.member.number.no.fan", "대상 회원번호가 팬이 아닐 시"),
    팬메모조회_실패("C006", "member.fan.memo.select.fail", "팬 메모조회 실패 시"),

    //팬 메모 저장
    팬메모저장_성공("0", "member.fan.memo.save.success", "팬 메모저장 성공 시"),
    팬메모저장_요청회원_회원아님("-1", "member.fan.memo.save.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    팬메모저장_대상회원_회원아님("-2", "member.fan.memo.save.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    팬메모저장_실패("C006", "member.fan.memo.save.fail", "팬 메모저장 실패 시"),

    //팬 리스트 편집
    팬리스트편집_성공("0", "member.fan.list.edit.success", "팬 리스트 편집 성공 시"),
    팬리스트편집_요청회원_회원아님("-1", "member.fan.list.edit.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    팬리스트편집_실패("C006", "member.fan.list.edit.fail", "팬 리스트 편집 실패 시"),

    //스타리스트 조회
    스타리스트조회_성공("C001", "member.star.list.select.success", "스타리스트 조회 성공 시"),
    스타리스트조회_없음("0", "member.star.list.no.star.success", "스타 없을 시"),
    스타리스트조회_요청회원_회원아님("-1", "member.star.list.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    스타리스트조회_대상회원_회원아님("-2", "member.star.list.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    스타리스트조회_실패("C006", "member.star.list.select.fail", "스타리스트 조회 실패 시"),

    //스타 메모 조회
    스타메모조회_성공("0", "member.star.memo.select.success", "스타 메모조회 성공 시"),
    스타메모조회_요청회원_회원아님("-1", "member.star.memo.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    스타메모조회_대상회원_회원아님("-2", "member.star.memo.select.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    스타메모조회_대상회원_팬아님("-3", "member.star.memo.select.object.member.number.no.star", "대상 회원번호가 스타가 아닐 시"),
    스타메모조회_실패("C006", "member.star.memo.select.fail", "스타 메모조회 실패 시"),

    //스타 메모 저장
    스타메모저장_성공("0", "member.star.memo.save.success", "스타 메모저장 성공 시"),
    스타메모저장_요청회원_회원아님("-1", "member.star.memo.save.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    스타메모저장_대상회원_회원아님("-2", "member.star.memo.save.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    스타메모저장_실패("C006", "member.star.memo.save.fail", "스타 메모저장 실패 시"),

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

    //기프티콘 당첨자리스트 조회
    기프티콘_당첨자리스트조회("C001", "gifticon.win.list.select.success", "기프티콘 당첨자 조회 성공 시"),
    기프티콘_당첨자리스트없음("0", "gifticon.win.list.select.no.success", "당첨자 없을 시"),
    기프티콘_당첨자리스트조회_회원아님("-1", "gifticon.win.list.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    기프티콘_당첨자리스트조회_실패("C006", "gifticon.win.list.select.fail", "기프티콘 당첨자 조회 실패 시"),

    //게스트 신청
    게스트신청_성공("0", "guest.propose.success", "게스트 신청 성공 시"),
    게스트신청_회원아님("-1", "guest.propose.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트신청_방번호없음("-2", "guest.propose.no.room.number", "방번호 없을 시"),
    게스트신청_종료된방번호("-3", "guest.propose.end.room.number", "종료된 방 번호일 시"),
    게스트신청_청취자아님("-4", "guest.propose.no.room.member", "방 청취자 아닐 시"),
    게스트신청_신청불가상태("-5", "guest.propose.no.state", "신청 불가 상태 시"),
    게스트신청_이미신청중("-6", "guest.propose.already", "이미 신청중일 시"),
    게스트신청_실패("C006", "guest.propose.fail", "게스트 신청 실패 시"),

    //게스트 신청 취소
    게스트신청취소_성공("0", "guest.propose.cancel.success", "게스트 신청 취소 성공 시"),
    게스트신청취소_회원아님("-1", "guest.propose.cancel.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트신청취소_방번호없음("-2", "guest.propose.cancel.no.room.number", "방번호 없을 시"),
    게스트신청취소_종료된방번호("-3", "guest.propose.cancel.end.room.number", "종료된 방 번호일 시"),
    게스트신청취소_청취자아님("-4", "guest.propose.cancel.no.room.member", "방 청취자 아닐 시"),
    게스트신청취소_불가상태("-5", "guest.propose.cancel.no.state", "취소 불가 상태 시"),
    게스트신청취소_신청상태아님("-6", "guest.propose.cancel.no.propose.state", "신청상태가 아닐 시"),
    게스트신청취소_실패("C006", "guest.propose.cancel.fail", "게스트 신청 취소 실패 시"),

    //게스트 관리 조회
    게스트관리조회_성공("C001", "guest.management.select.success", "게스트 관리 조회 성공 시"),
    게스트리스트_없음("0", "guest.management.select.no.success", "게스트 리스트 없을 시"),
    게스트관리조회_요청회원번호_회원아님("-1", "guest.management.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트관리조회_방번호없음("-2", "guest.management.select.no.room.number", "방번호 없을 시"),
    게스트관리조회_종료된방번호("-3", "guest.management.select.end.room.number", "종료된 방 번호일 시"),
    게스트관리조회_청취자아님("-4", "guest.management.select.no.room.member", "방 청취자 아닐 시"),
    게스트관리조회_권한없음("-5", "guest.management.select.no.auth", "조회 권한 없을 시"),
    게스트관리조회_실패("C006", "guest.management.select.fail", "게스트 관리 조회 실패 시"),

    //게스트 초대
    게스트초대_성공("0", "guest.invite.success", "게스트 초대 성공 시"),
    게스트초대_회원아님("-1", "guest.invite.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트초대_초대대상_회원아님("-2", "guest.invite.object.member.number.error", "초대대상 회원번호가 정상회원이 아닐 시"),
    게스트초대_방번호없음("-3", "guest.invite.no.room.number", "방번호 없을 시"),
    게스트초대_종료된방번호("-4", "guest.invite.end.room.number", "종료된 방 번호일 시"),
    게스트초대_요청회원_방에없음("-5", "guest.invite.room.join.no", "요청회원 방에 없을 시"),
    게스트초대_권한없음("-6", "guest.invite.no.auth", "초대 권한 없을 시"),
    게스트초대_초대회원_방에없음("-7", "guest.invite.object.room.join.no", "초대 대상이 방에 없을 시"),
    게스트초대_초대불가("-8", "guest.invite.impossible", "대상 초대불가일 시"),
    게스트초대_이미초대중("-10", "guest.invite.already", "대상 이미 초대중일 시"),
    게스트초대_이미존재_제한("-11", "guest.invite.already.guest", "게스트가 이미 존재중일 시"),
    게스트초대_다른회원_초대중("-12", "guest.invite.another.member.inviting", "다른회원 초대중일 시"),
    게스트초대_실패("C006", "guest.invite.fail", "게스트 초대 실패 시"),

    //게스트 초대 취소
    게스트초대취소_성공("0", "guest.invite.cancel.success", "게스트 초대 성공 시"),
    게스트초대취소_회원아님("-1", "guest.invite.cancel.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트초대취소_초대대상_회원아님("-2", "guest.invite.cancel.object.member.number.error", "초대대상 회원번호가 정상회원이 아닐 시"),
    게스트초대취소_방번호없음("-3", "guest.invite.cancel.no.room.number", "방번호 없을 시"),
    게스트초대취소_종료된방번호("-4", "guest.invite.cancel.end.room.number", "종료된 방 번호일 시"),
    게스트초대취소_요청회원_방에없음("-5", "guest.invite.cancel.room.join.no", "요청회원 방에 없을 시"),
    게스트초대취소_권한없음("-6", "guest.invite.cancel.no.auth", "초대 취소 권한 없을 시"),
    게스트초대취소_초대회원_방에없음("-7", "guest.invite.cancel.object.room.join.no", "초대 대상이 방에 없을 시"),
    게스트초대취소_초대취소불가("-8", "guest.invite.cancel.impossible", "대상 초대 취소불가일 시"),
    게스트초대취소_초대중아님("-10", "guest.invite.cancel.no.inviting", "대상 초대중 아닐 시"),
    게스트초대취소_실패("C006", "guest.invite.cancel.fail", "게스트 초대 실패 시"),

    //게스트 초대 수락
    게스트초대수락_성공("0", "guest.invite.ok.success", "게스트 초대 수락 성공 시"),
    게스트초대수락_회원아님("-1", "guest.invite.ok.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트초대수락_방번호없음("-2", "guest.invite.ok.no.room.number", "방번호 없을 시"),
    게스트초대수락_종료된방번호("-3", "guest.invite.ok.end.room.number", "종료된 방 번호일 시"),
    게스트초대수락_요청회원_방에없음("-4", "guest.invite.ok.room.join.no", "요청회원 방에 없을 시"),
    게스트초대수락_불가("-5", "guest.invite.ok.impossible", "대상 초대 수락불가일 시"),
    게스트초대수락_초대상태아님("-6", "guest.invite.no.inviting", "대상 초대중 아닐 시"),
    게스트초대수락_실패("C006", "guest.invite.ok.fail", "게스트 초대 수락 실패 시"),

    //게스트 초대 거절
    게스트초대거절_성공("0", "guest.invite.refuse.success", "게스트 거절 성공 시"),
    게스트초대거절_회원아님("-1", "guest.invite.refuse.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    게스트초대거절_초대대상_회원아님("-2", "guest.invite.refuse.object.member.number.error", "거절 대상 회원번호가 정상회원이 아닐 시"),
    게스트초대거절_방번호없음("-3", "guest.invite.refuse.no.room.number", "방번호 없을 시"),
    게스트초대거절_종료된방번호("-4", "guest.invite.refuse.end.room.number", "종료된 방 번호일 시"),
    게스트초대거절_요청회원_방에없음("-5", "guest.invite.refuse.room.join.no", "요청회원 방에 없을 시"),
    게스트초대거절_권한없음("-6", "guest.invite.refuse.no.auth", "거절 취소 권한 없을 시"),
    게스트초대거절_초대회원_방에없음("-7", "guest.invite.refuse.object.room.join.no", "거절 대상이 방에 없을 시"),
    게스트초대거절_초대취소불가("-8", "guest.invite.refuse.impossible", "대상 거절 취소불가일 시"),
    게스트초대거절_초대중아님("-10", "guest.invite.refuse.no.inviting", "대상 초대중 아닐 시"),
    게스트초대거절_실패("C006", "guest.invite.refuse.fail", "게스트 거절 실패 시"),

    //이모티콘 조회
    이모티콘조회_성공("0", "emoticon.select.success", "이모티콘 조회 성공 시"),
    이모티콘조회_회원아님("-1", "emoticon.select.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    이모티콘조회_실패("C006", "emoticon.select.fail", "이모티콘 조회 실패 시"),
    이모티콘조회_없음("C007", "emoticon.select.no", "이모티콘 없을 시"),
    카테고리_없음("C008", "emoticon.category.select.no", "카테고리 없을 시"),

    //게스트리스트조회
    게스트리스트조회_성공("C001", "guest.select.success", "게스트 리스트 조회 성공 시"),
    게스트리스트조회_없음("0", "guest.select.no", "게스트 리스트 없을 시"),
    게스트리스트조회_실패("C006", "guest.select.fail", "게스트 리스트 조회 실패 시"),

    //문자발송
    문자발송_성공("0", "sms.send.success", "문자 발송 성공 시"),
    문자발송_실패("C006", "sms.send.fail", "문자 발송 실패 시"),

    //메인 랭킹
    메인_랭킹조회_성공("C001", "main.ranking.select.success", "랭킹 조회 성공 시"),
    메인_랭킹조회_내역없음("0", "main.ranking.no.ranking.success", "랭킹 내역 없을 시"),
    메인_랭킹조회_요청회원_회원아님("-1", "main.ranking.member.number.error", "요청회원번호가 회원 아닐 시"),
    메인_랭킹조회_실패("C006", "main.ranking.select.fail", "랭킹 조회 실패 시"),

    //방송방 왕회장 & 팬 랭킹 3 조회
    방송방_팬랭킹조회_성공("C001", "broadcast.fan.ranking.select.success", "팬 랭킹 조회 성공 시"),
    방송방_팬랭킹조회_팬없음("0", "broadcast.fan.ranking.no.fan.success", "팬 없을 시"),
    방송방_팬랭킹조회_방번호없음("-1", "broadcast.fan.ranking.room.number.no", "방 번호 없을 시"),
    방송방_팬랭킹조회_방종료됨("-1", "broadcast.fan.ranking.room.end", "방이 종료되었을 시"),
    방송방_팬랭킹조회_실패("C006", "broadcast.fan.ranking.select.fail", "팬 랭킹 조회 실패 시"),

    //메인 좋아요 랭킹
    메인_좋아요랭킹조회_성공("C001", "main.good.ranking.select.success", "좋아요 랭킹 조회 성공 시"),
    메인_좋아요랭킹조회_내역없음("0", "main.good.ranking.no.ranking.success", "좋아요 내역 없을 시"),
    메인_좋아요랭킹조회_실패("C006", "main.good.ranking.select.fail", "좋아요 랭킹 조회 실패 시"),
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
