package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomStoryAddVo extends P_ApiVo {

    /* input */
    private String mem_no;      // 회원 번호
    private String room_no;     // 방송방 번호
    private String contents;    // 사연
    private String dj_mem_no;   // dj 회원번호
    private String plus_yn;     // 플러스 아이템 여부
    
    /* output */
    private int passTime;       // 최근등록후 지난시간(초)
}
