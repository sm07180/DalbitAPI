package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_FanboardAddVo {

    /* input */
    private String star_mem_no;     // 팬보드 스타 회원번호
    private String writer_mem_no;   // 댓글 작성자 회원번호
    private int depth;              // 1. 댓글 2. 대댓글
    private int board_no;           // 대댓글인경우 댓글의 그룹번호
    private String contents;        // 댓글내용

}
