package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.RoomExitVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_SummaryListenerVo extends P_ApiVo {
    private String room_no;
    private String mem_no;

    private String title;
    private int os_type;
    private String mem_nick;
    private String mem_sex;
    private String image_profile;

    public P_SummaryListenerVo(){}
    public P_SummaryListenerVo(RoomExitVo roomExitVo, HttpServletRequest request){
        this.room_no = roomExitVo.getRoomNo();
        this.mem_no = MemberVo.getMyMemNo(request);
    }
}
