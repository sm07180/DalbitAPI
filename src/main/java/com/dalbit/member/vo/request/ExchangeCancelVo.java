package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class ExchangeCancelVo {
    @NotNull(message = "{\"ko_KR\" : \"환전번호를\"}")
    private Integer exchangeIdx;
}
