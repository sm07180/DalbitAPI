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
    @Builder.Default private String nickName="test0012";
    @Builder.Default private String memSex="m";
    @Builder.Default private int age=30;
    @Builder.Default private String memId="i9rbkp8t";
    @Builder.Default private String backgroundImage="null";
    @Builder.Default private String profileImage="http://profileImage.profileImage.com/test0012.jpg";
    @Builder.Default private String profileMsg="null";
    @Builder.Default private int level=1;
    @Builder.Default private int grade=1;
    @Builder.Default private int fanCount=0;
    @Builder.Default private int starCount=0;
    @Builder.Default private int enableFan=1;

}
