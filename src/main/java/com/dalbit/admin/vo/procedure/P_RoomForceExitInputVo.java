package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomForceExitInputVo extends P_ApiVo {
    private int memLogin;
    private String mem_no;
    private String room_no;
    private String start_date;
    private String roomExit;
    private String opName;
}
