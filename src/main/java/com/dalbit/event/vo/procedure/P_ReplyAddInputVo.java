package com.dalbit.event.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_ReplyAddInputVo {

    /* Input */
    private String mem_no;		//이벤트 댓글 작성 회원번호
    private int depth;			//1: 댓글, 2:대댓글
    private int event_idx;		//이벤트 번호
    private String contents;		//내용
    private String op_name; //작성자


}
