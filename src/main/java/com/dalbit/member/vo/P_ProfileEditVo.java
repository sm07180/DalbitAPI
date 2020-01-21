package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_ProfileEditVo {
    private String mem_no;
    private String memSex;              			  //성별 (m/f)
    private String nickName;                          //닉네임
    private String name;                              //이름
    private String birthYear;                         //생년
    private String birthMonth;                        //생월
    private String birthDay;                          //생일
    private String profileImage;                      //프로필이미지 패스
    private String profileImageGrade;                 //프로필이미지 구글 선정성
    private String backgroundImage;                   //배경이미지 패스
    private String backgroundImageGrade;              //배경이미지 구글 선정성
    private String profileMsg;                        //메시지

}
