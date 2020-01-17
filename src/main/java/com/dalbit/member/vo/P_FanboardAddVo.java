package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_FanboardAddVo {

    @Builder.Default private String star_mem_no = "11577931027280";
    @Builder.Default private String writer_mem_no = "11577950603958";
    @Builder.Default private int depth = 1;
    @Builder.Default private int board_no = 1;
    @Builder.Default private String contents = "댓글~~~";

}
