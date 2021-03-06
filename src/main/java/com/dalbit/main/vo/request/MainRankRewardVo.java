package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MainRankRewardVo {

    @NotNull(message = "{\"ko_KR\" : \"랭킹 구분을\"}")
    @Min(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 3)
    private Integer rankSlct;

    @NotNull(message = "{\"ko_KR\" : \"랭킹 타입을\"}")
    @Min(message = "{\"ko_KR\" : \"랭킹 타입을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"랭킹 타입을\"}", value = 2)
    private Integer rankType;

}
