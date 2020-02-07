package com.dalbit.broadcast.vo.procedure;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomGiftVo {
    private String mem_no;          //요청 회원번호
    private String room_no;         //해당 방 번호
    private String gifted_mem_no;   //선물받을 회원 번호
    private String item_no;         //선물할 아이템 번호
    private int item_cnt;           //아이템 개수
}
