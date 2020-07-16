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

    @NotBlank(message = "{\"ko_KR\" : \"휴대폰번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"휴대폰번호를\"}")
    private String phoneNo;

    @NotNull(message = "{\"ko_KR\" : \"인증구분을\"}")
    @Min(message = "{\"ko_KR\" : \"인증구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"인증구분을\"}", value = 1)
    private Integer authType;

    private String sendPhoneNo;
    private int code;
    private int CMID;
    private String umId;
    private String text;
    private String memNo;

}
