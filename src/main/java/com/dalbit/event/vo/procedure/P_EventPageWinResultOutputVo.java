package com.dalbit.event.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_EventPageWinResultOutputVo {
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
