package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomNoticeVo {
    
    /* 공지사항 조회, 삭제 공통 input */
    private String mem_no;      // 회원 번호
    private String room_no;     // 방송방 번호
}
