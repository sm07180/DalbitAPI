package com.dalbit.common.vo;

import lombok.Getter;

@Getter
public enum ResMessage {
    /*
     * 성공 : 00000
     * 실패 : 99999
     *
     * Validation Msssage : 10001 ~
     * Service Logic : 30001 ~
     * Etc Message : 80001 ~
     * System Error : 90001 ~
     * */
    C00000("00000","SUCCESS"),

    C10001("10001","회원번호(MemNo)가 없습니다."),
    C10002("10002","입력된 값이 존재하지 않습니다."),

    // 11월 이벤트
    C30001("30001", "11월 이벤트 경품 번호 없음"),
    C30002("30002", "보유중인 경품 응모권이 부족합니다."), // 11월 이벤트 응모권수 부족
    C30003("30003", "이벤트가 종료되었습니다."), // 11월 이벤트 종료

    // 깐부 이벤트
    C30004("30004", "이미 깐부가 있는 회원입니다."), // 신청대상자 깐부있음
    C30005("30005", "깐부는 최대 2명에게 신청 가능합니다.\n신청 취소 후 다시 접근해 주세요."), // 신청건수초과
    C30006("30006", "해당 회원은 탈퇴/정지된 회원입니다."), // 신청대상자 탈퇴&정지회원
    C30007("30007", "이벤트 기간이 아닙니다."), // 이벤트기간 아님
    C30008("30008", "신청내역이 없습니다."), // 신청내역 없음
    C30009("30009", "상대방이 이미 신청을 수락해 깐부가 맺어졌습니다."), // 이미 수락된 데이터
    C30010("30010", "나에게 신청을 한 회원입니다."), // 상대가 나에게 이미 신청
    C30011("30011", "이미 동일 회원에게 신청을 했습니다."), // 동일회원에게 이미 신청
    C30012("30012", "로그인 후 신청해주세요."), // 회원 아님
    C30013("30013", "깐부가 없습니다."), // 깐부 없음
    C30014("30014", "이미 지급되었습니다."), // 이미 지급됨
    C30015("30015", "구슬 개수가 부족합니다."), // 구슬 개수 부족
    C30016("30016", "배팅할 구슬 개수가 부족합니다."), // 배팅 구슬 개수 부족
    C30017("30017", "하루 2회만 베팅 가능합니다."), // 하루 2회만 베팅 가능
    C30018("30018", "배팅 가능한 구슬 개수를 초과했습니다."), // 배팅 가능한 구슬 개수 초과

    // 추억의 뽑기 이벤트
    C30101("30101", "일부 결과가 처리가 실패 했습니다."), // 뽑기 이벤트
    C30102("30102", "300개에 해당하는 이벤트 상품을 모두 획득하셨습니다."), // 뽑기 이벤트

    C30210("30210", "사연 내용 체크 에러"), // 사연 내용 체크 에러
    C30211("30211", "사연을 입력해주세요."), // 사연 내용 없음
    C30212("30212", "사연 내용을 초과했습니다."), // 사연 길이 초과
    C30213("30213", "사연 내용에 금지어가 있습니다."), // 사연 내용 금지어 포함
    C30214("30214", "삭제할 사연이 없습니다."), // 데이터 없음
    C30215("30215", "유효하지 않은 사연 번호입니다."), // 사연번호 없음
    C30216("30216", "보상 지급 대상이 아닙니다."), // 트리 점수 미달 (50점)
    C30217("30217", "트리 완성 보상을 받기 위해서는 본인인증이 필요합니다."), // 본인 미인증
    C30218("30218", "이미 보상을 받은 계정이 있습니다."), // 이미 인증받은 번호로 달 받음
    
    // 트리 이벤트
    C30301("30301", "하루 등록할 수 있는 사연 개수가 초과되었습니다."), // 도배 방지

    // 이벤트 공용
    C39001("39001", "이미 보상을 받은 계정이 있습니다."),
    C39002("39002", "보상을 받기 위해서는 본인인증이 필요합니다."),
    C39003("39003", "이미 보상을 받았습니다."),
    C39004("39004", "보상이 존재하지 않습니다."),
    C39005("39005", "보상을 받기 위한 조건이 성립되지 않습니다."),
    C39006("39006","이용할 수 있는 조건을 초과했습니다."),
    C39007("39007","이벤트에 참여할 수 없는 계정입니다."),

    // 법정대리인 이메일 등록
    C40001("40001", "이미 동의 되었습니다."), // 이미 동의된 데이터
    C40002("40002", "나이가 맞지 않습니다."), // 나이 안맞음
    C40003("40003", "본인인증되지 않은 계정입니다."), // 미인증
    C40004("40004", "법정대리인이 미성년자입니다."), // 법정대리인 미성년자 인증
    C40005("40005", "등록된 이메일이 없습니다."), // 법정대리인 이메일 등록 안함

    // 아이템 지급
    C50001("50001", "회원 및 아이템 수 없음"),

    //팀 관련
    C60001("60001", "팀가입 되어 있습니다."),
    C60002("60002", "재생성 시간 미경과 하였습니다."),
    C60003("60003", "이미 팀이 있습니다."),
    C60004("60004", "레벨이 부족합니다."),
    C60005("60005", "팀이름 중복 입니다."),
    C60006("60006", "재생성 시간 미경과 하였습니다."),
    C60007("60007", "해당 팀이 없습니다."),
    C60008("60008", "수정횟수 초과 되었습니다."),
    C60009("60009", "팀삭제권한이 없습니다."),
    C60010("60010", "미가입 회원 입니다."),
    C60011("60011", "정원초과 하였습니다."),
    C60012("60012", "가입신청 미허용"),
    C60013("60013", "이미 초대된 회원 입니다."),
    C60014("60014", "이미 신청한 팀입니다."),
    C60015("60015", "팀에 가입되어있습니다."),
    C60016("60016", "신청 내역이 없습니다."),
    C60017("60017", "권한이 없습니다."),
    C60018("60018", "출석완료"),
    C60019("60019", "대표설정 뱃지수 초과 되었습니다."),
    C60020("60020", "수정시간 초과 되었습니다."),
    C60021("60021", "팀 미가입 상태입니다."),
    C60022("60022", "뱃지 조건 미달성 하였습니다."),

    C99994("99994", "No Data"),
    C99995("99995","Invalid cookie"),
    C99996("99996","File Does Not Exist"),
    C99997("99997","DB Server Error"),
    C99998("99998","API Server Error"),
    C99999("99999","FAIL");

    //

    private String code;    //코드 : 숫자4자리
    private String codeNM;  //코드설명 : 영문 or 한글

    ResMessage(String code, String codeNM) {
        this.code = code;
        this.codeNM = codeNM;
    }
}
