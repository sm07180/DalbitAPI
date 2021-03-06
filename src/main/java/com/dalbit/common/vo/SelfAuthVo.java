package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SelfAuthVo {

    private String name="";         //이름
    private String phoneNo="";      //휴대폰번호
    private String phoneCorp="";    //통신사
    private String birthDay="";     //생년월일 8자리
    private String gender="";       //성별
    private String nation="";       //내외국인 0:내국인, 1:외국인

    private String cpId;            //회원사ID
    private String urlCode;         //URL코드
    private String certNum;         //요청번호
    private String date;            //요청일시
    private String certMet = "M";   //인증방법 ("M": 휴대폰, "C":신용카드, "P":공인인증서)
    private String plusInfo = "";   //추가DATA정보
    private String agreePeriod = "0";       //동의기간

    private String pageCode;        //페이지코드(1:결제(WEB), 2:결제(방송방), 3:방송방생성(RoomMake), 4:환전, 5:프로필, 6:방송하기&클립, 7:휴면해제, 8:방송방참여)
    private String authType;        //인증타입(0: 본인, 1:보호자)

    private String pushLink = "none";   // 기본 폼일때 인증 후 이동할 url 코드 (ex: main - 메인페이지, share - 공유 이벤트)
    private String memNo = "";      // 휴면 인증인 경우에 파라미터로 받은 memNo 사용

}
