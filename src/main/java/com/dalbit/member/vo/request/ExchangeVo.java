package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class ExchangeVo {

    @Min(600)
    private int byeol;
}
