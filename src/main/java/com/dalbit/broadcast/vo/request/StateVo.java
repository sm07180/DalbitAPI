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
    private boolean isMic;

    @NotNull
    private boolean isCall;

    private Integer state;

    public boolean getIsMic(){
        return this.isMic;
    }

    public boolean getIsCall(){
        return this.isCall;
    }
}
