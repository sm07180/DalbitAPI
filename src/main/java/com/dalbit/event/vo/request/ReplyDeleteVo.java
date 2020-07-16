package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReplyDeleteVo {

    @NotNull(message = "{\"ko_KR\" : \"이벤트를\"}")
    private int eventIdx;		    //이벤트 번호
    @NotNull(message = "{\"ko_KR\" : \"댓글번호를\"}")
    private int replyIdx;		    //댓글번호

}
