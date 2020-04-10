package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class RubyVo {

    @NotBlank
    private String memNo;

    @NotNull @Min(1)
    private int dal;
}
