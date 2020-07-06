package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.request.PushClickVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_PushClickVo {

    public P_PushClickVo(){}
    public P_PushClickVo(PushClickVo pushClickVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setPush_idx(pushClickVo.getPushIdx());
    }

    private String mem_no;
    private String push_idx;

}
