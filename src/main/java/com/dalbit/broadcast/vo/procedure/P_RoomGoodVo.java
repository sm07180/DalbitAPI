package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomGoodVo {

    private String mem_no;          //좋아요 누른 회원번호
    private String room_no;         //좋아요 방 번호
    private String good_count;      //좋아요 누적 총 개수
}
