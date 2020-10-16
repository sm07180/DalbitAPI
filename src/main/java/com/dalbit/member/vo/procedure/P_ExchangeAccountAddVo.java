package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ExchangeAccountAddVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ExchangeAccountAddVo {
    public P_ExchangeAccountAddVo(){}
    public P_ExchangeAccountAddVo(ExchangeAccountAddVo exchangeAccountAddVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setAccount_no(exchangeAccountAddVo.getAccountNo());
        setAccount_name(exchangeAccountAddVo.getAccountName());
        setBank_code(exchangeAccountAddVo.getBankCode());
        setBank_name(exchangeAccountAddVo.getBankName());
    }

    private String mem_no;
    private String account_no;
    private String account_name;
    private String bank_code;
    private String bank_name;

}
