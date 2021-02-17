package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class OpenEventVo {
    @NotNull(message = "{\"ko_KR\" : \"팬스타 구분을\"}")
    @Min(message = "{\"ko_KR\" : \"팬스타 구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"팬스타 구분을\"}", value = 2)
    private Integer slctType;

    private int eventNo;
}
