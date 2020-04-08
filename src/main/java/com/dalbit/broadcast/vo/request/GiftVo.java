package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class GiftVo {

    @NotBlank
    private String roomNo;

    @NotBlank
    private String memNo;

    @NotBlank
    private String itemNo;

    @NotNull @Min(1)
    private Integer itemCnt;

    @NotBlank
    private String isSecret;
}
