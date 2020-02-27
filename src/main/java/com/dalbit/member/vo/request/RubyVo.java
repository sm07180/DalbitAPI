package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class RubyVo {

    @NotBlank
    private String memNo;
    @NotNull
    private int dal;
}
