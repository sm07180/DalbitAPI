package com.dalbit.socket.vo;

import com.dalbit.broadcast.vo.request.RoomMemberInfoVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_RoomMemberInfoSelectVo {

    public P_RoomMemberInfoSelectVo(){}
    public P_RoomMemberInfoSelectVo(RoomMemberInfoVo roomMemberInfoVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(roomMemberInfoVo.getRoomNo());
    }

    private String mem_no;
    private String room_no;
}
