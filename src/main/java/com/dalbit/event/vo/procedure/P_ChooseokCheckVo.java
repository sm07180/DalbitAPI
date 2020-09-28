package com.dalbit.event.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ChooseokCheckVo {
    private String mem_no;

    public P_ChooseokCheckVo(){}
    public P_ChooseokCheckVo(HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
    }
}
