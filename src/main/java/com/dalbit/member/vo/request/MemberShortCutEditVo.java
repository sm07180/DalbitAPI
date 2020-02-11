package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberShortCutEditVo {

    @NotBlank
    private String order;
    @NotBlank
    private String text;
    @NotBlank
    private boolean isOn;
}
