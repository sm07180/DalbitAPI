package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class SmsCheckOutVo {

    @NotNull
    private int CMID;
}
