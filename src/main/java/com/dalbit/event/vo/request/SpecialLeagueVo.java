package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class SpecialLeagueVo {

    @NotNull(message = "{\"ko_KR\" : \"기수를\"}")
    private Integer roundNo;
}
