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
    @NotNull @Min(1) @Max(3)
    private Integer state;
}
