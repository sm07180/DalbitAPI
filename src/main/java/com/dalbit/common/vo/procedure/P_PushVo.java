package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.request.PushVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_PushVo {

    public P_PushVo(){}
    public P_PushVo(PushVo pushVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctPush(pushVo.getPushType());
        setTitle(pushVo.getTitle());
        setContents(pushVo.getContents());
    }

    private String mem_no;
    private Integer slctPush;
    private String title;
    private String contents;
}
