package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class SmsCheckVo {

    @NotNull
    private Integer CMID;

    @NotNull
    private Integer code;
}
