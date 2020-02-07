package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomNoticeEditVo {

    /* input */
    private String mem_no;      // 회원번호
    private String room_no;     // 방송방번호
    private String notice;      // 공지사항

}
