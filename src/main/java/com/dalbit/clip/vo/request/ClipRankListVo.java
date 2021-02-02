package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ClipRankListVo {
    @NotNull(message = "{\"ko_KR\" : \"기간 선택을\"}")
    @Min(message = "{\"ko_KR\" : \"기간 선택을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"기간 선택을\"}", value = 2)
    private Integer rankType;

    @NotBlank(message = "{\"ko_KR\" : \"요청날짜를\"}")
    @NotNull(message = "{\"ko_KR\" : \"요청날짜를\"}")
    private String rankingDate;

    private Integer page;
    private Integer records;

}
