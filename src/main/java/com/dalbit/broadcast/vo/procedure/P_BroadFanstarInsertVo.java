package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_BroadFanstarInsertVo extends P_ApiVo {

    private String fan_mem_no;                  //팬번호
    private String star_mem_no;                 //스타번호
    private String room_no;                     //방번호
    private int type=0;                         // 0:일반, 1:추천DJ

}
