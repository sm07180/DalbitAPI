package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AwardVoteResultVo {

    @NotNull(message = "{\"ko_KR\" : \"회원구분을\"}")
    @Min(message = "{\"ko_KR\" : \"회원구분을\"}", value = 1)    //DJ
    @Max(message = "{\"ko_KR\" : \"회원구분을\"}", value = 2)    //FAN
    private Integer slctType;

    @NotBlank(message = "{\"ko_KR\" : \"투표년도를\"}")
    @NotNull(message = "{\"ko_KR\" : \"투표년도를\"}")
    private String year;
}
