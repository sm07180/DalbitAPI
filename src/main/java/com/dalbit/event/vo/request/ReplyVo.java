package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReplyVo {

    @NotNull(message = "{\"ko_KR\" : \"이벤트를\"}")
    private Integer eventIdx;              //이벤트 번호

}
