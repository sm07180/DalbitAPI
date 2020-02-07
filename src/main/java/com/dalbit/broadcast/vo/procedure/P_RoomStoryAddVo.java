package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomStoryAddVo {

    /* input */
    private String mem_no;      // 회원 번호
    private String room_no;     // 방송방 번호
    private String contents;    // 사연

    /* output */
    private int passTime;       // 최근등록후 지난시간(초)
}
