package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.FreezeVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_FreezeVo {
    private String mem_no;
    private String room_no;
    int freezeMsg;

    public P_FreezeVo(){}

    public P_FreezeVo(FreezeVo freezeVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(freezeVo.getRoomNo());
        setFreezeMsg("true".equals(freezeVo.getIsFreeze()) || "1".equals(freezeVo.getIsFreeze()) ? 1 : 0);
    }
}
