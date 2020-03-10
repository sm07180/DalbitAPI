package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
@Getter
@Setter
public class MemberReportAddVo {

    @NotBlank
    private String memNo;
    @NotNull
    private Integer reason;

    private String cont;
    private String roomNo;
}
