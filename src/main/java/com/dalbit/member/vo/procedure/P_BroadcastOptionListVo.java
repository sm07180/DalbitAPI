package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadcastOptionListVo {

    public P_BroadcastOptionListVo(){}
    public P_BroadcastOptionListVo(HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
    }

    /* Input */
    private String mem_no;

    /* Output */
    private int idx;
    private String contents;
}
