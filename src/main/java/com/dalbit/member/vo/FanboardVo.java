package com.dalbit.member.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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

    private String mem_no;
    private String star_mem_no;

}


