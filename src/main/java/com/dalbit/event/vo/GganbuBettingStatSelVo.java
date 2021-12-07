package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuBettingStatSelVo {
    private String gganbu_no;
    private String betting_slct;
    private int betting_cnt;
    private String oddWinProbability;
    private String evenWinProbability;
}
