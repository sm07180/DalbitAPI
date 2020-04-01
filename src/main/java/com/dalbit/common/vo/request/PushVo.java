package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class PushVo {

    @NotNull
    private Integer pushType;
    @NotBlank
    private String title;
    @NotBlank
    private String contents;

}
