package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.BroadcastOptionAddVo;
import com.dalbit.member.vo.request.BroadcastWelcomeMsgAddVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadcastWelcomeMsgAddVo extends P_ApiVo {
    private String mem_no;
    private String welcomeMsg;

    public P_BroadcastWelcomeMsgAddVo(){}
    public P_BroadcastWelcomeMsgAddVo(BroadcastOptionAddVo broadcastOptionAddVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setWelcomeMsg(broadcastOptionAddVo.getContents());
    }
}
