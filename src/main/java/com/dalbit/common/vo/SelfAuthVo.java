package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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

    private String pageCode;        //페이지코드(1:결제(WEB), 2:결제(방송방), 3:방송방생성)
    private String authType;        //인증타입(0: 본인, 1:보호자)
    private String agreeTerm;       //동의기간

}
