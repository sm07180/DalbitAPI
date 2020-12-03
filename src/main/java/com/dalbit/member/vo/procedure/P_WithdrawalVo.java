package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.WithdrawalVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_WithdrawalVo extends P_ApiVo {

    private String mem_no;
    private String mem_userid;

    public P_WithdrawalVo(){}
    public P_WithdrawalVo(WithdrawalVo withdrawalVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setMem_userid(withdrawalVo.getUid());
    }

}
