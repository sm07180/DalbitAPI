package com.dalbit.member.vo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_FanboardDeleteVo {

    /* input */
    private String star_mem_no;     // 팬보드 스타 회원번호
    private String delete_mem_no;   // 댓글 삭제자 회원번호
    private int board_idx;          // 댓글 인덱스번호
}
