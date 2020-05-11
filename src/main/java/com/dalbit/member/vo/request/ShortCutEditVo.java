package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class ShortCutEditVo {

    @NotNull
    private Integer orderNo;
    @NotBlank @Size(max = 4)
    private String order;
    @NotBlank
    private String text;

    private String isOn;
}
