package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class EventPagePrizeReceiveVo {

    @NotNull(message = "{\"ko_KR\" : \"이벤트를\"}")
    private Integer eventIdx;
    @NotNull(message = "{\"ko_KR\" : \"경품을\"}")
    private Integer prizeIdx;
    @NotNull(message = "{\"ko_KR\" : \"수령방법을\"}")
    private Integer receiveWay;
}
