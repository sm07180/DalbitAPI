package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MoonVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MoonVo {
    public P_MoonVo(){}
    public P_MoonVo(MoonVo moonVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(moonVo.getRoomNo());
    }

    private String mem_no;
    private String room_no;
}
