package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class ExchangeVo {

    @Min(message = "{\"ko_KR\" : \"별을\"}", value = 570)
    private int byeol;
}
