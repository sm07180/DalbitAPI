package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class GuestAddVo {

    @NotBlank
    private String roomNo;

    @NotBlank
    private String memNo;

    @NotBlank
    private String title;
}
