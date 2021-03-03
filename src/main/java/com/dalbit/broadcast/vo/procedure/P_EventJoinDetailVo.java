package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.EventJoinDetailVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_EventJoinDetailVo extends P_ApiVo {
    public P_EventJoinDetailVo(){}
    public P_EventJoinDetailVo(EventJoinDetailVo eventJoinDetailVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(eventJoinDetailVo.getSlctType());
    }
    private String mem_no;
    private int slctType;
}
