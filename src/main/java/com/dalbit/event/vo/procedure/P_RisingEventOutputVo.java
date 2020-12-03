package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RisingEventOutputVo extends P_ApiVo {
    /* Output */
    private int myRank;
    private int myPoint;

    private int expPoint;
    private int listenerPoint;
    private int giftPoint;
    private int goodPoint;
}
