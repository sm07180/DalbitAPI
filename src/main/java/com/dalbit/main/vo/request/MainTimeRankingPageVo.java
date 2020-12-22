package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MainTimeRankingPageVo {

    @NotNull(message = "{\"ko_KR\" : \"랭킹 구분을\"}") // DJ, FAN
    @Min(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 2)
    private Integer rankSlct;

    private Integer page;
    private Integer records;
    private String rankingDate;
}
