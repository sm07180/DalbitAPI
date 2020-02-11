package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BoosterVo {

    @NotBlank
    private String roomNo;
    @NotBlank
    private String itemNo;
    @NotNull
    private Integer itemCnt;
}
