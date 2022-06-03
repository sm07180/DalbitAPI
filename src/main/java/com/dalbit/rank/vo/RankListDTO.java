package com.dalbit.rank.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class RankListDTO {

    @NotNull(message = "{\"ko_KR\" : \"랭킹 구분을\"}")
    @Min(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 3)
    private Integer rankSlct;   // [1=DJ, 2=FAN, 3=CUPID, 4=TEAM]

    @NotNull(message = "{\"ko_KR\" : \"기간 선택을\"}")
    @Min(message = "{\"ko_KR\" : \"기간 선택을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"기간 선택을\"}", value = 4)
    private Integer rankType; //[1=일간, 2=주간, 3=월간, 4=연간]

    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;

    @Min(message = "{\"ko_KR\" : \"조회 건수를\"}", value = 1)
    private Integer records;

    @NotBlank(message = "{\"ko_KR\" : \"요청날짜를\"}")
    @NotNull(message = "{\"ko_KR\" : \"요청날짜를\"}")
    private String rankingDate;

    private String prevRankingDate;

}
