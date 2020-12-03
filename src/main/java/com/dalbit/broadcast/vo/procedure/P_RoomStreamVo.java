package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomStreamVo extends P_ApiVo {

    /* INPUT */
    private int memLogin;                       //회원 로그인 상태(1: 회원, 0: 비회원)
    private String mem_no;                      //참가하려는 회원번호
    private String room_no;                     //참가하려는 방 번호
}
