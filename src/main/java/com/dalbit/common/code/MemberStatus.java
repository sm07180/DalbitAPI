package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum MemberStatus implements Status {


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
    로그인실패_휴면상태("-8", "login.sleep", "로그인 실패 시 - 휴면상태"),
    로그인오류("C006", "login.error", "로그인 오류 시"),

    //로그아웃
    로그아웃성공("0", "logout.success", "로그아웃 성공 시"),
    로그아웃실패_진행중인방송("0", "logout.mybroadcast.ing", "로그아웃을 시도했지만 방송을 진행하고 있을 때"),

    //차단관련 운영메시지
    차단_이용제한("0", "block.member.restriction", "운영자에 의해 차단되어 이용제한 시"),

    //휴대폰 sms 인증
    인증번호요청("0", "sms.number.request.success", "인증번호 요청 성공 시"),
    인증확인("1", "sms.number.check.success", "인증확인 성공 시"),
    인증시간초과("-1", "sms.check.timeout", "인증시간 초과 시"),
    인증번호불일치("-2", "sms.check.number.error", "인증 번호 일치하지 않을 시"),
    인증CMID불일치("-3", "sms.check.cmid.error", "인증 CMID 일치하지 않을 시"),
    인증번호요청_회원아님("-4", "sms.number.request.member.no", "인증번호 요청"),
    인증번호요청_유효하지않은번호("-5", "sms.phone.number.error.no", "유효하지않은 휴대폰 번호일 시"),
    인증번호요청실패("C006", "sms.number.request.fail", "인증번호 요청 실패 시"),
    인증실패("C006", "sms.check.fail", "인증 실패 시"),

    //본인인증요청
    본인인증요청("0", "self.auth.request.success", "본인인증 요청 성공 시"),
    본인인증요청실패("C006", "self.auth.request.fail", "본인인증 요청 실패 시"),

    //본인인증확인
    본인인증확인("0", "self.auth.response.success", "본인인증 확인 성공 시"),
    본인인증검증_비정상접근("-1", "self.auth.response.check.error", "본인인증 비정상적인 접근 시"),
    본인인증실패("C006", "self.auth.response.fail", "본인인증 확인 실패 시"),
    본인인증14세미만("C007", "self.auth.14age.limit", "14세 미만 회원일 시"),
    보호자인증20세미만("C008", "self.auth.20age.limit", "보호자 20세 미만일 시"),

    //진행중인방송방 체크
    방송중인DJ체크_방송중("1", "dj.broadcast.onair", "DJ가 방송중일 시"),
    방송중인DJ체크_방송중아님("0", "dj.broadcast.offair", "DJ가 방송중이 아닐 시"),
    방송중인DJ체크_잘못된회원번호("-1", "dj.notMember", "DJ가 방송중이 아닐 시"),

    //회원가입
    회원가입안됨("1", "member.join.not", "회원가입 안된 상태 시"),
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
    비밀번호변경_인증요청_회원아님("2", "member.change.password.sms.request.fail", "비밀번호 변경 인증요청 실패 - 회원이 아닌경우"),
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
    팬등록_본인불가("-4", "member.insert.fan.not.me", "팬 등록 본인 불가 시"),
    팬등록_차단회원불가("-5", "member.insert.fan.black.list.no", "차단 회원 팬 등록 불가 시"),
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
    회원정보보기_차단회원불가("-5", "member.black.list.no", "차단회원 불가 시"),
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
    알림무음("0", "mypage.member.notify.edit.all.silent.success", "알림 모드 무음"),
    알림소리("0", "mypage.member.notify.edit.all.sound.success", "알림 모드 소리"),
    알림진동("0", "mypage.member.notify.edit.all.vibration.success", "알림 모드 진동"),
    전제알림수신_ON("0", "mypage.member.notify.edit.all.on.success", "전체 알림 수신 ON"),
    전제알림수신_OFF("0", "mypage.member.notify.edit.all.off.success", "전체 알림 수신 OFF"),
    마이스타방송시작_ON("0", "mypage.member.notify.edit.mystar.broadcast.start.on.success", "마이스타 방송 시작 알림 ON"),
    마이스타방송시작_OFF("0", "mypage.member.notify.edit.mystar.broadcast.start.off.success", "마이스타 방송 시작 알림 OFF"),
    마이스타클립등록_ON("0", "mypage.member.notify.edit.mystar.clip.add.on.success", "마이스타 클립 등록 알림 ON"),
    마이스타클립등록_OFF("0", "mypage.member.notify.edit.mystar.clip.add.off.success", "마이스타 클립 등록 알림 OFF"),
    마이스타방송공지_ON("0", "mypage.member.notify.edit.mystar.broadcast.notice.on.success", "마이스타 방송 공지 ON"),
    마이스타방송공지_OFF("0", "mypage.member.notify.edit.mystar.broadcast.notice.off.success", "마이스타 방송 공지 OFF"),
    내클립_ON("0", "mypage.member.notify.edit.my.clip.on.success", "내 클립 ON"),
    내클립_OFF("0", "mypage.member.notify.edit.my.clip.off.success", "내 클립 OFF"),
    신규팬추가_ON("0", "mypage.member.notify.edit.new.fan.on.success", "신규 팬 추가 ON"),
    신규팬추가_OFF("0", "mypage.member.notify.edit.new.fan.off.success", "신규 팬 추가 OFF"),
    팬보드신규글등록_ON("0", "mypage.member.notify.edit.fanboard.new.on.success", "팬보드 신규 글 등록 ON"),
    팬보드신규글등록_OFF("0", "mypage.member.notify.edit.fanboard.new.off.success", "팬보드 신규 글 등록 OFF"),
    팬보드댓글등록_ON("0", "mypage.member.notify.edit.fanboard.reply.on.success", "팬보드 댓글 등록 ON"),
    팬보드댓글등록_OFF("0", "mypage.member.notify.edit.fanboard.reply.off.success", "팬보드 댓글 등록 OFF"),
    선물도착_ON("0", "mypage.member.notify.edit.gift.on.success", "선물 도착 ON"),
    선물도착_OFF("0", "mypage.member.notify.edit.gift.off.success", "선물 도착 OFF"),
    일대일문의답변도착_ON("0", "mypage.member.notify.edit.one.on.success", "1:1문의 답변 도착 ON"),
    일대일문의답변도착_OFF("0", "mypage.member.notify.edit.one.off.success", "1:1문의 답변 도착 OFF"),
    서비스알림_ON("0", "mypage.member.notify.edit.service.on.success", "서비스 알림 ON"),
    서비스알림_OFF("0", "mypage.member.notify.edit.service.off.success", "서비스 알림 OFF"),
    방송시작알림_ON("0", "mypage.member.notify.edit.broadcast.start.on.success", "방송 시작 알림 ON"),
    방송시작알림_OFF("0", "mypage.member.notify.edit.broadcast.start.off.success", "방송 시작 알림 OFF"),
    메시지알림_ON("0", "mypage.member.notify.edit.mailbox.on.success", "메시지 알림 ON"),
    메시지알림_OFF("0", "mypage.member.notify.edit.mailbox.off.success", "메시지 알림 OFF"),
    팀알림_ON("0", "mypage.member.notify.edit.team.on.success", "팀 알림 ON"),
    팀알림_OFF("0", "mypage.member.notify.edit.team.off.success", "팀 알림 OFF"),

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
    팬보드_스타회원번호_회원아님("-2", "fanboard.list.starNo.notMember", "스타 회원번호가 회원이 아님"),
    팬보드_조회오류("C006", "fanboard.list.error", "팬보드 댓글 리스트 조회 실패"),

    //프로필 팬보드 상세 조회
    팬보드상세조회성공("C001", "fanboard.detail.select.success", "팬보드 상세 조회 성공"),
    팬보드상세조회정보없음("-1", "fanboard.detail.select.fail.writeNo", "팬보드 상세 조회 글 없음"),
    팬보드상세조회실패("C006", "fanboard.detail.select.fail.error", "팬보드 정보 조회 실패"),

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
    팬보드_댓글수정실패_삭제댓글_수정불가("-5", "fanboard.edit.fail.delete.reply", "이미 삭제되어 수정 불가 시"),
    팬보드_댓글수정실패_수정권한없음("-6", "fanboard.edit.fail.authorization.not", "수정 권한이 없을 시"),
    팬보드_댓글수정실패("C006", "fanboard.edit.fail", "댓글 수정 실패 시"),

    //회원 마이페이지 팬보드 대댓글 보기
    팬보드_대댓글조회성공("C001", "fanboard.reply.comment.success", "팬보드 대댓글 조회 시"),
    팬보드_대댓글조회실패_대댓글없음("0", "fanboard.reply.comment.no.success", "대댓글 없을 시"),
    팬보드_대댓글조회실패_요청회원번호_회원아님("-1", "fanboard.reply.requestNo.notMember", "요청 회원번호가 회원이 아닐 시"),
    팬보드_대댓글조회실패_스타회원번호_회원아님("-2", "fanboard.reply.starNo.notMember", "스타 회원번호가 회원이 아닐 시"),
    팬보드_대댓글조회오류("C006", "fanboard.reply.error", "팬보드 대댓글 보기 실패 시"),
    //팬 랭킹 조회
    팬랭킹조회_성공("C001", "member.fan.ranking.select.success", "팬 랭킹 조회 성공 시"),
    팬랭킹조회_팬없음("0", "member.fan.ranking.no.fan.success", "팬 없을 시"),
    팬랭킹조회_요청회원_회원아님("-1", "member.fan.ranking.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    팬랭킹조회_대상회원_회원아님("-2", "member.fan.ranking.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    팬랭킹조회_실패("C006", "member.fan.ranking.select.fail", "팬 랭킹 조회 실패 시"),
    // 회원 레벨 업 확인
    레벨업확인_성공("0", "member.levelup.check.success", "레벨 업 확인 성공 시"),
    레벨업확인_요청회원번호_회원아님("-1", "member.levelup.check.member.number.error", "요청회원번호가 회원 아닐 시"),
    레벨업확인_레벨업_없음("-2", "member.levelup.check.no", "레벨업 없을 시"),
    레벨업확인_실패("C006", "member.levelup.check.fail", "레벨 업 확인 실패 시"),
    //회원탈퇴
    회원탈퇴_성공("0", "member.withdrawal.success", "회원탈퇴 성공 시"),
    회원탈퇴_회원아님("-1", "member.withdrawal.member.number.error", "요청회원번호 회원 아닐 시"),
    회원탈퇴_이미탈퇴("-2", "member.withdrawal.already", "이미 탈퇴한 회원일 시"),
    회원탈퇴_방접속중("-3", "member.withdrawal.playing", "방송방 접속중일 시"),
    회원탈퇴_실패("C006", "member.withdrawal.fail", "회원탈퇴 실패 시"),
    //팬 조회
    팬조회_성공("C001", "member.fan.list.select.success", "팬 랭킹 조회 성공 시"),
    팬조회_팬없음("0", "member.fan.list.no.fan.success", "팬 없을 시"),
    팬조회_요청회원_회원아님("-1", "member.fan.list.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    팬조회_대상회원_회원아님("-2", "member.fan.list.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    팬조회_실패("C006", "member.fan.list.select.fail", "팬 랭킹 조회 실패 시"),
    //알림 읽음처리
    알림읽음_성공("0", "member.notification.read.success", "알림 읽음 성공 시"),
    알림읽음_회원아님("-1", "member.notification.read.member.number.error", "요청회원번호 회원 아닐 시"),
    알림읽음_실패("C006", "member.notification.read.fail", "알림 읽음 실패 시"),
    //구글 로그인
    구글로그인_성공("0", "google.login.success", "성공"),
    구글로그인_토큰없음("-1", "google.login.blank.token", "토큰없음"),
    구글로그인_토큰인증실패("-2", "google.login.invalid.token", "인증실패"),
    구글로그인_오류("C006", "google.login.error", "오류"),
    //페이스북 로그인
    페이스북로그인_성공("0", "facebook.login.success", "성공"),
    페이스북로그인_토큰없음("-1", "facebook.login.blank.token", "토큰없음"),
    페이스북로그인_토큰인증실패("-2", "facebook.login.invalid.token", "인증실패"),
    페이스북로그인_오류("C006", "facebook.login.error", "오류"),
    // 소셜 로그인
    소셜로그인_성공("0", "social.login.success", "성공"),
    소셜로그인_오류("C006", "social.login.error", "오류"),
    //스페셜DJ
    스페셜DJ_신청성공("0", "special.dj.apply.success", "스페셜DJ 신청 성공 시"),
    스페셜DJ_회원아님("-1", "special.dj.apply.no.member.success", "요청회원번호 회원 아닐 시"),
    스페셜DJ_이미신청("-2", "special.dj.apply.already.member", "이미 신청하였을 경우"),
    스페셜DJ_기간아님("-3", "special.dj.apply.passed.period", "기간이 지났을 경우"),
    스페셜DJ_신청실패("C006", "special.dj.apply.fail", "스페셜DJ 신청 실패 시"),
    //프로필이미지 초기화
    프로필이미지초기화_성공("0", "profile.image.init.success", "프로필이미지 초기화 성공 시"),
    프로필이미지초기화_실패("C006", "profile.image.init.fail", "프로필이미지 초기화 실패 시"),
    //닉네임 초기화
    닉네임초기화_성공("0", "member.nickname.init.success", "닉네임 초기화 성공 시"),
    닉네임초기화_실패("C006", "member.nickname.init.fail", "닉네임 초기화 실패 시"),
    //신고처리
    신고처리_성공("0", "declaration.operate.success", "신고처리 성공 시"),
    신고처리_에러("C006", "declaration.fail.server.error", "신고처리에서 에러 발생 시"),
    //신고내역 조회
    신고목록조회_데이터없음("0", "declaration.list.no.data", "신고목록 조회 데이터가 없을 시"),
    신고목록조회_성공("C001", "declaration.list.select.success", "신고목록 조회 시"),
    신고목록조회_에러("C006", "declaration.list.server.error", "신고목록 조회에서 에러 발생 시"),
    //신고내역 상세조회
    신고상세조회_공지번호없음("-1", "declaration.detail.no.data", "신고목록 조회 데이터가 없을 시"),
    신고상세조회_성공("0", "declaration.detail.select.success", "신고목록 상세조회 시"),
    신고상세조회_에러("C006", "declaration.detail.server.error", "신고상세 조회에서 에러 발생 시"),
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
    //개인좋아요랭킹
    개인좋아요랭킹_성공("C001", "member.good.list.select.success", "스타리스트 조회 성공 시"),
    개인좋아요랭킹_없음("0", "member.good.list.no.star.success", "스타 없을 시"),
    개인좋아요랭킹_요청회원_회원아님("-1", "member.good.list.member.number.error", "요청 회원번호가 정상회원이 아닐 시"),
    개인좋아요랭킹_대상회원_회원아님("-2", "member.good.list.object.member.number.error", "대상 회원번호가 정상회원이 아닐 시"),
    개인좋아요랭킹_실패("C006", "member.good.list.select.fail", "스타리스트 조회 실패 시"),
    //스페셜DJ 선정 이력 조회
    스페셜DJ선정내역조회_성공("C001", "special.dj.selection.select.success", "스페셜DJ 선정 이력 조회 성공 시"),
    스페셜DJ선정내역조회_없음("0", "special.dj.selection.select.no.success", "스페셜DJ 선정 이력 조회 없을 시"),
    //회원 휴면,탈퇴 예정일 조회
    휴면탈퇴_일자조회_성공("0", "sleep.withdrawal.select.date.success", "휴면탈퇴 일자조회 성공 시"),
    휴면탈퇴_일자조회_회원아님("-1", "sleep.withdrawal.select.date.member.number.error", "요청회원번호 회원 아닐 시"),
    휴면탈퇴_일자조회_실패("C006", "sleep.withdrawal.select.date.fail", "휴면탈퇴 일자조회 실패 시"),
    //가산점 조회
    가산점조회_성공("C001", "special.addpoint.select.success", "가산점 조회 성공 시"),
    가산점조회_없음("0", "special.addpoint.select.no.success", "가산점 내역 없을 시"),
    가산점조회_회원아님("-1", "special.addpoint.select.member.number.error", "요청회원번호 회원 아닐 시"),
    가산점조회_실패("C006", "special.addpoint.select.fail", "가산점 조회 실패 시"),
    //휴면 해제
    휴면해제_성공("0", "member.sleep.return.success", "휴면 해제 성공 시"),
    휴면해제_회원아님("-1", "member.sleep.return.member.number.error", "요청회원번호 회원 아닐 시"),
    휴면해제_상태아님("-2", "member.sleep.return.no.sleep.state", "휴면상태가 아닐 시"),
    휴면해제_실패("C006", "member.sleep.return.fail", "휴면 해제 실패 시"),
    //이미지신고
    이미지신고_성공("0", "image.report.success", "이미지 신고 성공 시"),
    이미지신고_요청회원아님("-1", "image.report.member.number.error", "요청회원번호 회원 아닐 시"),
    이미지신고_대상회원아님("-2", "image.report.target.member.number.error", "대상회원번호 회원 아닐 시"),
    이미지신고_이미신고("-3", "image.report.already", "이미 신고된 이미지"),
    이미지신고_방번호없음("-4", "image.report.no.room.number", "방번호가 없을 시"),
    이미지신고_이미지번호없음("-5", "image.report.no.idx", "이미지 번호 없을 시"),
    이미지신고_실패("C006", "image.report.fail", "이미지 신고 실패 시"),
    //친구초대
    초대코드_생성("0000", "invitaion.code.success", "초대코드 생성 성공 시"),
    초대코드_중복("C001", "invitaion.code.duplication", "초대코드 중복 발생 시"),
    친구코드_등록_성공("0000", "invitaion.friend.success", "친구코드 등록 성공 시"),
    친구코드_없음("C001", "invitaion.friendempty", "친구코드 없음"),
    친구코드_등록_에러("C002", "invitaion.friend.fail", "친구코드 등록 실패 시"),
    친구코드_중복_등록("C003", "invitaion.friend.fail", "친구코드 중복 등록 시도 시"),
    친구초대_참여대상_아님("C004", "invitaion.join.fail", "친구초대 참여대상 아님"),
    친구초대_기간_중복("C005", "invitaion.join.fail", "친구초대 가입내역, 중복아이디 대상"),
    친구초대_나이제한("C006", "invitaion.join.fail", "친구초대 나이제한"),
    휴면회원_본인인증_체크_성공("0", "sleep.mem.chk.upd.success", "휴면 회원 본인 인증 성공"),
    휴면회원_본인인증_결과없음("-1", "sleep.mem.chk.upd.no.auth", "휴면 회원 본인 인증 결과 없음"),
    휴면회원_본인인증_휴면상태아님("-2", "sleep.mem.chk.upd.no.sleep", "휴면 회원 본인 인증 휴면 상태 아님"),
    // TNK 콜백
    TNK_성공("0", "tnk.callback.success", "TNK 무료충전 성공 시"),
    TNK_회원아님("-1", "tnk.callback.member.number.error", "TNK 무료충전 회원 아닐 시"),
    TNK_이미받음("-2", "tnk.callback.already", "TNK 무료충전 이미 받은 보상 시"),
    TNK_실패("C006", "tnk.callback.fail", "TNK 무료충전 보상 실패 시");


    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    MemberStatus(String messageCode, String messageKey, String desc) {
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
