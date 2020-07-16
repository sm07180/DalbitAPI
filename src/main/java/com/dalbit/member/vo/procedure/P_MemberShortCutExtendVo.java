package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberShortCutExtendVo {

    private String mem_no;
    private int orderNo;                        //명령어 순번
    private String order;                       //명령어
    private String text;                        //내용

}
