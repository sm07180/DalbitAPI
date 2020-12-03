package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_EventPageWinListOutputVo extends P_ApiVo {
    /* Output */
    private int prizeRank;
    private int prizeCnt;
    private String prizeName;
    private String nickName;
}
