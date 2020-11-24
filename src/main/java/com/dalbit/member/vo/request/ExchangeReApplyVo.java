package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter @Setter
public class ExchangeReApplyVo {

    private int exchangeIdx;
    @Min(message = "{\"ko_KR\" : \"별을\"}", value = 570)
    private BigDecimal byeol;

    private String accountName;
    private String accountNo;
    private String bankCode;
}
