package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Getter @Setter
public class P_ExchangeVo extends P_ApiVo {

    private String mem_no;
    private BigDecimal byeol;
}
