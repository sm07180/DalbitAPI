package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.BroadcastOptionAddVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadcastTitleAddVo {
    private String mem_no;
    private String title;

    public P_BroadcastTitleAddVo(){}
    public P_BroadcastTitleAddVo(BroadcastOptionAddVo broadcastOptionAddVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setTitle(broadcastOptionAddVo.getContents());
    }
}
