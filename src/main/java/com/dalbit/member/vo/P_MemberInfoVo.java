package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_MemberInfoVo {

    @Builder.Default private String mem_no = "11577690655946";
    @Builder.Default private String target_mem_no = "11577931027280";
    @Builder.Default private String nickName="test0012";                                                //닉네임
    @Builder.Default private String memSex="m";                                                         //성별
    @Builder.Default private int age=30;                                                                //나이대
    @Builder.Default private String memId="i9rbkp8t";                                                   //자동생성된아이디8자
    @Builder.Default private String backgroundImage="null";                                             //배경이미지
    @Builder.Default private String profileImage="http://profileImage.profileImage.com/test0012.jpg";   //프로필이미지
    @Builder.Default private String profileMsg="null";                                                  //프로필메세지
    @Builder.Default private int level=1;                                                               //레벨
    @Builder.Default private int grade=1;                                                               //등급
    @Builder.Default private int fanCount=0;                                                            //팬으로 등록된 회원수
    @Builder.Default private int starCount=0;                                                           //스타로 등록된 회원수
    @Builder.Default private int enableFan=1;                                                           //팬등록 가능여부 (1:가능)

}
