package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ExchangeAccountDeleteVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ExchangeAccountDeleteVo extends P_ApiVo {
    public P_ExchangeAccountDeleteVo(){}
    public P_ExchangeAccountDeleteVo(ExchangeAccountDeleteVo exchangeAccountDeleteVo, HttpServletRequest request){
        setIdx(exchangeAccountDeleteVo.getIdx());
        setMemNo(MemberVo.getMyMemNo(request));
        setBeforeAccountNo(exchangeAccountDeleteVo.getBeforeAccountNo());
    }
    private int idx;
    private String memNo;
    private String beforeAccountNo;
}
