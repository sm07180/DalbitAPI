package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.RoomStreamIdVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_RoomStreamIdVo extends P_ApiVo {

    public P_RoomStreamIdVo(){}
    public P_RoomStreamIdVo(RoomStreamIdVo roomStreamIdVo, HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(roomStreamIdVo.getRoomNo());
    }

    private int memLogin;
    private String mem_no;
    private String room_no;
}
