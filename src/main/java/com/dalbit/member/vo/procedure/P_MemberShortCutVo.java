package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberShortCutVo {

    private String mem_no;

    private int orderNo;                             //순서번호
    private String order;                               //명령어
    private String text;                                //내용
    private String onOff;                               //사용여부
}
