package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyAddVo {

    private String memNo;		//이벤트 댓글 작성 회원번호
    private int depth;			//1: 댓글, 2:대댓글
    private int eventIdx;		//이벤트 번호
    private String content;		//내용

}
