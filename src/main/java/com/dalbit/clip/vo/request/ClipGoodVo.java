package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ClipGoodVo {
    @NotBlank(message = "{\"ko_KR\" : \"클립 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 번호를\"}")
    private String clipNo;

    @NotNull(message = "{\"ko_KR\" : \"좋아요 구분을\"}")
    @Min(message = "{\"ko_KR\" : \"좋아요 구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"좋아요 구분을\"}", value = 1)
    private Integer good;
}
