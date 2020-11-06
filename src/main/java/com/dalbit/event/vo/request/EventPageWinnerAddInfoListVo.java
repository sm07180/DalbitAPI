package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EventPageWinnerAddInfoListVo {

    @NotNull(message = "{\"ko_KR\" : \"이벤트를\"}")
    private int eventIdx;
    @NotNull(message = "{\"ko_KR\" : \"경품을\"}")
    private int prizeIdx;
}
