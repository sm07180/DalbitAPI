package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class FanboardEditVo {

    @NotBlank
    private String memNo;

    @NotNull
    private Integer boardIdx;

    @NotBlank
    private String content;


}
