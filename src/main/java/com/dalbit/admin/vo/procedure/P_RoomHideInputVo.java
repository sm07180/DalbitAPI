package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomHideInputVo extends P_ApiVo {
    // 방송방 숨김상태
    private String mem_no;
    private String room_no;
    private String title;
    private int hide;
}
