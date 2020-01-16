package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_FanstarInsertVo {

    @Builder.Default private String fanMemNo = "11577690655946";
    @Builder.Default private String starMemNo = "11577931027280";

}
