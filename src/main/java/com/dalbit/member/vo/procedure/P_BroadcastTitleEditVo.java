package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.BroadcastOptionEditVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadcastTitleEditVo {
    private String mem_no;
    private String title;
    private int titleIdx;

    public P_BroadcastTitleEditVo(){}
    public P_BroadcastTitleEditVo(BroadcastOptionEditVo broadcastOptionEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setTitle(broadcastOptionEditVo.getContents());
        setTitleIdx(broadcastOptionEditVo.getIdx());
    }
}
