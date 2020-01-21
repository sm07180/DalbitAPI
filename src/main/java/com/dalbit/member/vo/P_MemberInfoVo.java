package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberInfoVo {

    private String mem_no;
    private String target_mem_no;
    private String nickName;                                                        //닉네임
    private String memSex;                                                          //성별
    private int age;                                                                //나이대
    private String memId;                                                           //자동생성된아이디8자
    private String backgroundImage;                                                 //배경이미지
    private String profileImage;                                                    //프로필이미지
    private String profileMsg;                                                      //프로필메세지
    private int level;                                                               //레벨
    private int grade;                                                               //등급
    private int fanCount;                                                            //팬으로 등록된 회원수
    private int starCount;                                                           //스타로 등록된 회원수
    private int enableFan;                                                           //팬등록 가능여부 (1:가능)

}
