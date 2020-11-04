package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.RoomExitVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_SummaryDjVo {
    private String room_no;
    private String mem_no;

    private String title;
    private String mem_nick;
    private String mem_sex;
    private String image_profile;
    private long gold;

    public P_SummaryDjVo(){}
    public P_SummaryDjVo(RoomExitVo roomExitVo, HttpServletRequest request){
        this.room_no = roomExitVo.getRoomNo();
        this.mem_no = MemberVo.getMyMemNo(request);
    }
}
