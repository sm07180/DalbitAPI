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

    @Size (max = 256)
    private String cont;
    private String roomNo;
}
