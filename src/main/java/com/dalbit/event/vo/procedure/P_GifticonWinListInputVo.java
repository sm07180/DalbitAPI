package com.dalbit.event.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_GifticonWinListInputVo {

    public P_GifticonWinListInputVo(){}

    public P_GifticonWinListInputVo(HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
    }

    private String mem_no;
}
