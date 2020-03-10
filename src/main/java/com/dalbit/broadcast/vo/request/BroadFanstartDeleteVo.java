package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BroadFanstartDeleteVo {

    @NotBlank
    private String memNo;

}
