package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class ErrorLogVo {

    @NotBlank
    private String dataType;
    @NotBlank
    private String commandType;
    @NotBlank
    private String desc;
}
