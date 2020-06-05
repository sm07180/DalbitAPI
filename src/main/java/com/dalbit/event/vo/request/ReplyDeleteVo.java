package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReplyDeleteVo {

    @NotNull
    private int eventIdx;		    //이벤트 번호
    @NotNull
    private int replyIdx;		    //댓글번호

}
