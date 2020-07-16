package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class MainDjRankingVo {

    @NotNull(message = "{\"ko_KR\" : \"랭킹 구분을\"}")
    @Min(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 3)
    private Integer rankType;
    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회 건수를\"}", value = 1)
    private Integer records;


}
