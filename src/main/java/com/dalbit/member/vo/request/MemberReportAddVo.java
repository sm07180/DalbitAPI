package com.dalbit.member.vo.request;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberReportAddVo {

    @NotBlank
    private String memNo;
    @NotNull
    private int reason;
    @NotBlank
    private String cont;
}
