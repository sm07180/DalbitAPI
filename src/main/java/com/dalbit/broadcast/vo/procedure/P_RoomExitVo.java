package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomExitVo {

    private int memLogin;       //회원 로그인 상태(1: 회원, 0: 비회원)
    private String mem_no;      //나가려는 회원번호
    private String room_no;     //나가려는 방 번호
}
