package com.dalbit.clip.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MyClipVo extends P_ApiVo {

    public P_MyClipVo(){}
    public P_MyClipVo(HttpServletRequest request){
       setMem_no(MemberVo.getMyMemNo(request));
    }
    private String mem_no;
}
