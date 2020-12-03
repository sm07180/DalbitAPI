package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ExchangeAccountEditVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ExchangeAccountEditVo extends P_ApiVo {
    public P_ExchangeAccountEditVo(){}
    public P_ExchangeAccountEditVo(ExchangeAccountEditVo exchangeAccountEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setAccount_no(exchangeAccountEditVo.getAccountNo());
        setAccount_name(exchangeAccountEditVo.getAccountName());
        setBank_code(exchangeAccountEditVo.getBankCode());
        setBank_name(exchangeAccountEditVo.getBankName());
        setIdx(exchangeAccountEditVo.getIdx());
        setBeforeAccountNo(exchangeAccountEditVo.getBeforeAccountNo());
    }

    private String mem_no;
    private String account_no;
    private String account_name;
    private String bank_code;
    private String bank_name;
    private int idx;
    private String beforeAccountNo;

}
