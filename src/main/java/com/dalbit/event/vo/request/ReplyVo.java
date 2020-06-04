package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReplyVo {

    @NotNull
    private Integer eventIdx;              //이벤트 번호

}
