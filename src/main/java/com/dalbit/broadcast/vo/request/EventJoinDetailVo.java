package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class EventJoinDetailVo {
    @NotNull(message = "{\"ko_KR\" : \"이벤트구분을\"}")
    @Min(message = "{\"ko_KR\" : \"이벤트구분을\"}", value = 5)
    @Max(message = "{\"ko_KR\" : \"이벤트구분을\"}", value = 10)
    private Integer slctType;
}
