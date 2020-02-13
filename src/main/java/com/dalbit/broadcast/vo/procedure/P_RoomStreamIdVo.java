package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.RoomStreamIdVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_RoomStreamIdVo {

    public P_RoomStreamIdVo(){}
    public P_RoomStreamIdVo(RoomStreamIdVo roomStreamIdVo){
        setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo());
        setRoom_no(roomStreamIdVo.getRoomNo());
    }

    private int memLogin;
    private String mem_no;
    private String room_no;
}
