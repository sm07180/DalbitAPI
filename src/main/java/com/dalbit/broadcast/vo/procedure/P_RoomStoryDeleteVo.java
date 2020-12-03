package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomStoryDeleteVo extends P_ApiVo {

    private String mem_no;
    private String room_no;         // 방송방 번호
    private Integer story_idx;      // 사연 인덱스 번호

    private Integer page;
    private Integer records;
}
