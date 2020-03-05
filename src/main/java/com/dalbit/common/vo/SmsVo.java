package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class SmsVo {
    @NotBlank
    private String phoneNo;
    private String sendPhoneNo;
    private int code;
    private int cmId;

}