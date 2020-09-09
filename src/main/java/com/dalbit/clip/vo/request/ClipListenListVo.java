package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ClipListenListVo {

    @NotBlank(message = "{\"ko_KR\" : \"스타 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"스타 회원번호를\"}")
    private String memNo;

    @NotNull(message = "{\"ko_KR\" : \"청취구분을\"}")
    @Min(message = "{\"ko_KR\" : \"청취구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"청취구분을\"}", value = 2)
    private Integer slctType;       //0: 최근(최대 100개), 1: 좋아요, 2: 선물

    private Integer page;
    private Integer records;
}
