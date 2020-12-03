package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_RoomStateUpdateVo extends P_ApiVo {

    private String mem_no;
    private String room_no;
    private Integer state;
}
