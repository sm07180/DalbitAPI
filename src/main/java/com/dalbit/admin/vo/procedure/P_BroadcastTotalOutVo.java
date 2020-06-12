package com.dalbit.admin.vo.procedure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_BroadcastTotalOutVo {
    private int sum_createCnt;
    private int sum_broadcastingTime;
    private int sum_djCnt;
    private int sum_listenerCnt;
    private int sum_guestCnt;
    private int sum_giftCnt;
    private int sum_giftAmount;
}