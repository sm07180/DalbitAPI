package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MemberReceiveDeleteVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MemberReceiveDeleteVo extends P_ApiVo {
    public P_MemberReceiveDeleteVo(){}
    public P_MemberReceiveDeleteVo(MemberReceiveDeleteVo memberReceiveDeleteVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setMem_no_star(memberReceiveDeleteVo.getMemNo());
    }
    private String mem_no;
    private String mem_no_star;
}
