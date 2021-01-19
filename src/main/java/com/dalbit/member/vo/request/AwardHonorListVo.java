package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class AwardHonorListVo {

    @NotNull(message = "{\"ko_KR\" : \"구분을\"}")
    private Integer slctType;       //1: Dj, 2:Fan

    private String selectYear = "2020";

    private String memNo = "0";
}
