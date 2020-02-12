package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class FanboardReplyVo {

    @NotBlank
    private String memNo;

    @NotNull
    private Integer boardNo;

}
