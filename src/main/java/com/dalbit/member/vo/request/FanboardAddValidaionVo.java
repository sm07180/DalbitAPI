package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class FanboardAddValidaionVo {

    @NotBlank @Size(min = 14, max = 14)
    private String memNo;

    @NotNull @Min(value = 1, message = "1(댓글) 또는 2(대댓글) 를 입력해주세요.") @Max(value = 2, message = "1(댓글) 또는 2(대댓글) 를 입력해주세요.")
    private Integer depth;

    private Integer boardNo;

    @NotBlank
    private String content;


}
