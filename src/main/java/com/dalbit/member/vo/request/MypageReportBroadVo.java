package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter @Setter
public class MypageReportBroadVo {

    @NotNull
    @Min(0) @Max(4)
    private Integer dateType;
    @NotBlank
    private String startDt;
    @NotBlank
    private String endDt;
    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;
}
