package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter @Setter
public class MypageReportBroadVo {

    @NotNull(message = "{\"ko_KR\" : \"구분을\"}")
    @Min(message = "{\"ko_KR\" : \"구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"구분을\"}", value = 4)
    private Integer dateType;
    @NotBlank(message = "{\"ko_KR\" : \"시작일을\"}")
    @NotNull(message = "{\"ko_KR\" : \"시작일을\"}")
    private String startDt;
    @NotBlank(message = "{\"ko_KR\" : \"종료일을\"}")
    @NotNull(message = "{\"ko_KR\" : \"종료일을\"}")
    private String endDt;
    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회건수를\"}", value = 1)
    private Integer records;
}
