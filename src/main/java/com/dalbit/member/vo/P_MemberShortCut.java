package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberShortCut {

    private String mem_no;

    private String orderNo;                             //순서번호
    private String order;                               //명령어
    private String test;                                //내용
    private String onOff;                               //사용여부
}
