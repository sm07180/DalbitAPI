package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ExchangeCancelVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_ExchangeCancelVo {
    public P_ExchangeCancelVo(){}
    public P_ExchangeCancelVo(ExchangeCancelVo exchangeCancelVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setExchangeIdx(exchangeCancelVo.getExchangeIdx());
    }

    private String mem_no;
    private int exchangeIdx;
}
