package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class SmsVo {

    @NotBlank
    private String phoneNo;
    @NotNull @Min(0) @Max(1)
    private Integer authType;

    private String sendPhoneNo;
    private int code;
    private int CMID;
    private String umId;
    private String text;
    private String memNo;

}
