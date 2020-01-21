package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_ProfileInfo {

    private String mem_no;                      //회원번호
    private String target_mem_no;               //스타회원번호

    private String  memNo;                  //회원번호
    private String  nickNm;                 //닉네임
    private String  gender;                 //성별
    private int     age;                    //나이대
    private String  memId;                  //자동생성된아이디8자
    private String  bgImg;                  //배경이미지
    private String  profImg;                //프로필이미지
    private String  profMsg;                //프로필메세지
    private int     level;                  //레벨
    private int     fanCnt;                 //팬으로 등록된 회원수
    private int     starCnt;                //스타로 등록된 회원수
    private int     isFan;                  //팬등록 가눙여부(1: 가능)
    private int     exp;                    //현재경험치
    private int     expNext;                //다음 레벨업 필요 경험치
    private String  grade;                  //등급
}
