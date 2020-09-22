package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class FanRankingVo {

    @NotBlank(message = "{\"ko_KR\" : \"회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"회원번호를\"}")
    private String memNo;
    private int page;
    private int records;

    /*@NotNull(message = "{\"ko_KR\" : \"랭킹 구분을\"}")
    @Min(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"랭킹 구분을\"}", value = 2)*/
    private Integer rankSlct = 1;

    /*@NotNull(message = "{\"ko_KR\" : \"랭킹 타입을\"}")
    @Min(message = "{\"ko_KR\" : \"랭킹 타입을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"랭킹 타입을\"}", value = 2)*/
    private Integer rankType = 2;

}
