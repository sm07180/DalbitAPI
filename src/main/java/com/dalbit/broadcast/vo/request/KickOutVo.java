package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class KickOutVo {

    @NotBlank
    private String roomNo;

    @NotBlank
    private String blockNo;
}
