package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MainRankingPageVo {

    @NotNull(message = "{\"ko_KR\" : \"랭킹 구분을\"}") // DJ, FAN
    @Min(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 3)
    private Integer rankSlct;

    @NotNull(message = "{\"ko_KR\" : \"기간 선택을\"}")
    @Min(message = "{\"ko_KR\" : \"기간 선택을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"기간 선택을\"}", value = 3)
    private Integer rankType;

    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회 건수를\"}", value = 1)
    private Integer records;

    @NotBlank(message = "{\"ko_KR\" : \"요청날짜를\"}")
    @NotNull(message = "{\"ko_KR\" : \"요청날짜를\"}")
    private String rankingDate;




}
