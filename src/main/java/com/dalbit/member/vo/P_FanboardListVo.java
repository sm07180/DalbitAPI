package com.dalbit.member.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_FanboardListVo {

    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private String star_mem_no = "11577931027280";
    @Builder.Default private int pageNo = 1;
    @Builder.Default private int pageCnt = 5;

}
