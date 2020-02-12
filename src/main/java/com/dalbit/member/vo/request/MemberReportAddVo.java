package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberReportAddVo {

    @NotBlank
    private String memNo;
    @NotBlank
    private int reason;
    @NotBlank
    private String cont;
}
