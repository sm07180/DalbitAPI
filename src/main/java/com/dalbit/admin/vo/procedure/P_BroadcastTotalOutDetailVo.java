package com.dalbit.admin.vo.procedure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_BroadcastTotalOutDetailVo {
    private String date;
    private String daily;
    private String monthly;
    private int hour;
    private int createCnt;
    private int broadcastingTime;
    private int djCnt;
    private int listenerCnt;
    private int guestCnt;
    private int giftCnt;
    private int giftAmount;
}



