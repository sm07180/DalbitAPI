package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberReportAddVo {

    private String mem_no;
    private String reported_mem_no;                           //신고회원번호
    private int reason;                                       //신고사유
    private String etc;                                       //상세내용

}
