package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EventGoodVo {
    @NotNull(message = "{\"ko_KR\" : \"이벤트 번호를\"}")
    private Integer event_idx;
    @NotNull(message = "{\"ko_KR\" : \"상세 번호를\"}")
    private Integer add_idx;
    private String mem_no;
}
