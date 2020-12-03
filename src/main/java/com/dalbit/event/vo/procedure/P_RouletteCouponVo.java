package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_RouletteCouponVo extends P_ApiVo {

    public P_RouletteCouponVo(){}
    public P_RouletteCouponVo(HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
    }

    private String mem_no;
}
