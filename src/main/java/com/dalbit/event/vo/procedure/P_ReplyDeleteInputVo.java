package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_ReplyDeleteInputVo extends P_ApiVo {

    /* Input */
    private String mem_no;
    private int event_idx;		//이벤트 번호
    private int reply_idx;         //이벤트 댓글 번호

}
