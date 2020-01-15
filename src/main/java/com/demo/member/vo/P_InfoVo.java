package com.demo.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_InfoVo {

    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private String target_mem_no = "11577931027280";

}
