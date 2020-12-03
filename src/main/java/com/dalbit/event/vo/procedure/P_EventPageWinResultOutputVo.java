package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_EventPageWinResultOutputVo extends P_ApiVo {
    /* Output */
    private int prizeRank;
    private int prizeIdx;
    private String prizeName;
    private int prizeSlct;
    private int state;
    private int certificationYn;
    private int minorYn;
    private int receiveWay;
    private int receiveDal;
}
