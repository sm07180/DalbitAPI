package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuBettingStatSelVo {
    private String s_aGganbuNo;

    private String oddWinProbability;
    private String evenWinProbability;

    private String s_aBettingSlct = "a"; // 홀
    private String s_bBettingSlct = "b"; // 짝
    private int s_aBettingCnt;
    private int s_bBettingCnt;
}
