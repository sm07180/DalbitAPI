package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class FanstarDeleteVo {

    @NotBlank @Size(min = 14, max = 14)
    private String memNo;
}