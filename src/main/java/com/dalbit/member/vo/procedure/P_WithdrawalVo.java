package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.WithdrawalVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_WithdrawalVo {

    private String mem_no;
    private String mem_userid;

    public P_WithdrawalVo(){}
    public P_WithdrawalVo(WithdrawalVo withdrawalVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setMem_userid(withdrawalVo.getUid());
    }

}
