package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_MemberInfo {

    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private String target_mem_no = "11577931027280";
}
