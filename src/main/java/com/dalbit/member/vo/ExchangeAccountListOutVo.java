package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_ExchangeAccountListVo;
import lombok.Getter;
import lombok.Setter;

@Getter  @Setter
public class ExchangeAccountListOutVo {
    private String accountNo;
    private String accountName;
    private String bankCode;
    private String bankName;
    private int idx;

    public ExchangeAccountListOutVo(P_ExchangeAccountListVo target){
        setAccountNo(target.getAccountNo());
        setAccountName(target.getAccountName());
        setBankCode(target.getBankCode());
        setBankName(target.getBankName());
        setIdx(target.getIdx());
    }
}
