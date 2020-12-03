package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadcastWelcomeMsgListVo extends P_ApiVo {

    public P_BroadcastWelcomeMsgListVo(){}
    public P_BroadcastWelcomeMsgListVo(HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
    }

    /* Input */
    private String mem_no;

    /* Output */
    private String welcomeMsgIdx;
    private String welcomeMsg;
}
