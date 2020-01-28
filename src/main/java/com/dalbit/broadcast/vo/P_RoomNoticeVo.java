package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomNoticeVo {
    // 공지사항 가져오기, 공지사항 삭제에 쓰이는 vo

    /* input */
    private String mem_no;      // 회원 번호
    private String room_no;     // 방송방 번호

}
