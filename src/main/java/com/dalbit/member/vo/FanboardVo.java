package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FanboardVo {
    private int board_idx;
    private int board_no;
    private String writer_mem_no;
    private String nickName;
    private String profileImage;
    private String contents;
    private int replyCnt;
    private int status;
    private String writeDate;
}


