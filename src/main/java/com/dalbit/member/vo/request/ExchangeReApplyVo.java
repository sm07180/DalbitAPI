package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class ExchangeReApplyVo {

    private int exchangeIdx;
    @Min(message = "{\"ko_KR\" : \"별을\"}", value = 570)
    private int byeol;

}
