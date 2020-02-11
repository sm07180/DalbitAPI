package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class FanboardDelVo {

    @NotBlank @Size(min = 14, max = 14)
    private String memNo;

    @NotNull
    private Integer boardIdx;

}
