package com.demo.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_InfoVo {

    @Builder.Default private String memMo = "11577690655946";
    @Builder.Default private String targetMemNo = "11577931027280";

}
