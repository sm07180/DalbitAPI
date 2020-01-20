package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_MemberBlockAddVo {

    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private String blocked_mem_no = "11577690655946";
}
