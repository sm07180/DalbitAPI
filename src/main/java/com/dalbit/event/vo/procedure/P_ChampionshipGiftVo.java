package com.dalbit.event.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ChampionshipGiftVo {
    public P_ChampionshipGiftVo(){}
    public P_ChampionshipGiftVo(HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
    }

    private String mem_no;
}
