package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class StateVo {

    @NotBlank
    private String roomNo;

    @NotNull
    private String isMic;

    @NotNull
    private String isCall;

    @NotNull
    private String isAnt;

    private Integer state;
}
