package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_FanboardEditVo {

    /* input */
    private String star_mem_no;     // 팬보드 스타 회원번호
    private String edit_mem_no;   // 댓글 수정자 회원번호
    private Integer board_idx;       // 댓글 인덱스 번호
    private String contents;        // 댓글내용

}
