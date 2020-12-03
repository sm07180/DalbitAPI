package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberShortCutVo extends P_ApiVo {

    private String mem_no;

    private int orderNo;        //순서번호
    private String order;       //명령어
    private String text;        //내용
    private String onOff;       //사용여부(on:사용중, off:사용기간만료)
    private String endDate;     //사용만료일자

}
