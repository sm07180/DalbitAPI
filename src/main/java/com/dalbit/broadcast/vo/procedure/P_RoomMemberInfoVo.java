package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.RoomMemberInfoVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_RoomMemberInfoVo {

    public P_RoomMemberInfoVo(){}
    public P_RoomMemberInfoVo(RoomMemberInfoVo roomMemberInfoVo){
        setMem_no(MemberVo.getMyMemNo());
        setTarget_mem_no(roomMemberInfoVo.getMemNo());
        setRoom_no(roomMemberInfoVo.getRoomNo());
    }

    private String mem_no;
    private String target_mem_no;
    private String room_no;
}
