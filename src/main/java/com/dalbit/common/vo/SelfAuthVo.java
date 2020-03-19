package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class SelfAuthVo {

    @NotBlank
    private String name;            //이름
    @NotBlank
    private String phoneNo;         //휴대폰번호
    @NotBlank
    private String phoneCorp;       //통신사
    @NotBlank
    private String birthDay;        //생년월일 8자리
    @NotBlank
    private String gender;          //성별
    @NotBlank
    private String nation;          //내외국인 0:내국인, 1:외국인


    private String cpId;            //회원사ID
    private String urlCode;         //URL코드
    private String certNum;         //요청번호
    private String date;            //요청일시
    private String certMet = "M";   //인증방법 ("M": 휴대폰, "C":신용카드, "P":공인인증서)
    private String plusInfo = "";   //추가DATA정보
}
