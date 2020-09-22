package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.BroadcastOptionDeleteVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadcastTitleDeleteVo {
    private String mem_no;
    private int titleIdx;

    public P_BroadcastTitleDeleteVo(){}
    public P_BroadcastTitleDeleteVo(BroadcastOptionDeleteVo broadcastOptionDeleteVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setTitleIdx(broadcastOptionDeleteVo.getIdx());
    }
}
