package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MoonVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MoonCheckVo {
    public P_MoonCheckVo(){}
    public P_MoonCheckVo(MoonVo moonVo){
        setRoom_no(moonVo.getRoomNo());
    }
    private String room_no;
}
