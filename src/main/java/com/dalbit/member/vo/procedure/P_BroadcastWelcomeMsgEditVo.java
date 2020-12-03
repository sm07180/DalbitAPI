package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.BroadcastOptionEditVo;
import com.dalbit.member.vo.request.BroadcastWelcomeMsgEditVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadcastWelcomeMsgEditVo extends P_ApiVo {
    private String mem_no;
    private String welcomeMsg;
    private int welcomeMsgIdx;

    public P_BroadcastWelcomeMsgEditVo(){}
    public P_BroadcastWelcomeMsgEditVo(BroadcastOptionEditVo broadcastOptionEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setWelcomeMsg(broadcastOptionEditVo.getContents());
        setWelcomeMsgIdx(broadcastOptionEditVo.getIdx());
    }
}
