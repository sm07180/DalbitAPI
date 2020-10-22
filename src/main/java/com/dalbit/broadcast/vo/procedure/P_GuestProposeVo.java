package com.dalbit.broadcast.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_GuestProposeVo {

    private String mem_no;
    private String room_no;

    public P_GuestProposeVo(){}
    public P_GuestProposeVo(String roomNo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(roomNo);
    }

}
