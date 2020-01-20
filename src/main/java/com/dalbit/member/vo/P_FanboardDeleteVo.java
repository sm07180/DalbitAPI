package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class P_FanboardDeleteVo {
    @Builder.Default private String star_mem_no = "11577931027280";
    @Builder.Default private String delete_mem_no = "11577690655946";
    @Builder.Default private int board_idx = 5;
}