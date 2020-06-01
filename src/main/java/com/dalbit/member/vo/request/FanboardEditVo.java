package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class FanboardEditVo {

    @NotBlank
    private String memNo;

    @NotNull
    private Integer boardIdx;

    @NotBlank @Size(max = 100)
    private String content;


}
