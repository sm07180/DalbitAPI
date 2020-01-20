package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_MemberReportAddVo {

    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private String reported_mem_no = "11577690655946";             //신고회원번호
    @Builder.Default private int reason = 1;                                        //신고사유
    @Builder.Default private String etc = "신고내용";                               //상세내용

}
