package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class P_FanboardListVo {

    /* Input */
    private String mem_no;
    private String star_mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int board_idx;
    private int board_no;
    private String writer_mem_no;
    private String nickName;
    private String memSex;
    private String profileImage;
    private String contents;
    private int replyCnt;
    private int status;
    private Date writeDate;

}