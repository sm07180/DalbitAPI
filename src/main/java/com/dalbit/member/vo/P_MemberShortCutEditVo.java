package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberShortCutEditVo {

    private String mem_no;

    private String order_1;                       //첫번째 명령
    private String text_1;                        //첫번째 내용
    private String onOff_1;                       //첫번째 사용여부 (on/off)
    private String order_2;                       //두번째 명령
    private String text_2;                        //두번째 내용
    private String onOff_2;                       //두번째 사용여부 (on/off)
    private String order_3;                       //세번째 명령
    private String text_3;                        //세번째 내용
    private String onOff_3;                       //세번째 사용여부 (on/off)
}
