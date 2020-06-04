package com.dalbit.event.vo.procedure;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class P_ReplyListOutputVo {
    /* Output */
    private int reply_idx;				// '순서'
    private int event_idx;		    // '이벤트번호'
    private String write_mem_no;			// '회원번호'
    private int depth;			        // '깊이'
    private String contents;		// '내용'
    private int status;			    // '상태(1:정상 2:삭제)'
    private Date writeDate;		    // '생성일자'
    private Date updateDate;	// '수정일시'
    private String op_name;		// '수정자'
    private String req_mem_no;			// 요청 mem_no

    private String userId;
    private String nickName;
    private String memSex;
    private String profileImage;                   //프로필이미지 패스
}
