package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ExchangeAccountAddVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ExchangeAccountListVo extends P_ApiVo {
    public P_ExchangeAccountListVo(){}
    public P_ExchangeAccountListVo(HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
    }

    private String mem_no;
    private String accountNo;
    private String accountName;
    private String bankCode;
    private String bankName;
    private int idx;

}
