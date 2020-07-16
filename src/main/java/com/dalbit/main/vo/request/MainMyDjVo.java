package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class MainMyDjVo {

    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회 건수를\"}", value = 1)
    private Integer records;
}
