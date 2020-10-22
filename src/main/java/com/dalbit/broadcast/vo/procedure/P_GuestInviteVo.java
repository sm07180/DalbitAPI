package com.dalbit.broadcast.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_GuestInviteVo {

    public P_GuestInviteVo(){}
    public P_GuestInviteVo(String memNo, String roomNo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setTarget_mem_no(memNo);
        setRoom_no(roomNo);
    }

    /* Input */
    private String mem_no;
    private String room_no;
    private String target_mem_no;

}
