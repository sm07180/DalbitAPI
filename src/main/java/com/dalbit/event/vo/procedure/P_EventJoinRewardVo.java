package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.EventJoinRewardVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_EventJoinRewardVo extends P_ApiVo {
    public P_EventJoinRewardVo(){}
    public P_EventJoinRewardVo(EventJoinRewardVo eventJoinRewardVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(eventJoinRewardVo.getSlctType());
    }
    private String mem_no;
    private int slctType;

}
