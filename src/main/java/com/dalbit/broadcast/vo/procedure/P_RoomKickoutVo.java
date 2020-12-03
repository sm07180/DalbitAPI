package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_RoomKickoutVo extends P_ApiVo {

    private String mem_no;              //요청회원번호
    private String room_no;             //방송방번호
    private String blocked_mem_no;      //강퇴회원번호

}
