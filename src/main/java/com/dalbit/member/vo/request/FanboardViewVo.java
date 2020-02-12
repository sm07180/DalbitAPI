package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class FanboardViewVo {

    @NotBlank
    private String memNo;

    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;
}
