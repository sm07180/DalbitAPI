package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_GuestInviteOkVo extends P_ApiVo {

    public P_GuestInviteOkVo(){}
    public P_GuestInviteOkVo(String roomNo, int yesNo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(roomNo);
        setYes_no(yesNo);
    }

    /* Input */
    private String mem_no;
    private String room_no;
    private int yes_no;

}
