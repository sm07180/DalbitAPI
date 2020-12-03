package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomJoinTokenVo extends P_ApiVo {

    private String room_no;     //참가하려는 방 번호

}
