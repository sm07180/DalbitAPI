package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberNotifyEditVo {

    private String mem_no;      //요청 회원번호
    private int all_ok;         //전체알림
    private int set_1;          //마이 스타 알림
    private int set_2;          //선물 받은 달 알림
    private int set_3;          //팬 알림
    private int set_4;          //댓글 알림
    private int set_5;          //달빛 라이브 알림
    private int set_6;          //push 알림
    private int set_7;          //좋아요 알림

}
