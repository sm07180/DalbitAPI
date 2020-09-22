package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.BroadcastOptionDeleteVo;
import com.dalbit.member.vo.request.BroadcastWelcomeMsgDeleteVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadcastWelcomeMsgDeleteVo {
    private String mem_no;
    private int welcomeMsgIdx;

    public P_BroadcastWelcomeMsgDeleteVo(){}
    public P_BroadcastWelcomeMsgDeleteVo(BroadcastOptionDeleteVo broadcastOptionDeleteVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setWelcomeMsgIdx(broadcastOptionDeleteVo.getIdx());
    }
}
