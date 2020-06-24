package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdbrixVo {

    public AdbrixVo(){}
    public AdbrixVo(String eventName, String sex, int age, String connectTime, String connectCnt, String broadcastCreateYn, String broadcastJoinYn, String payYn){
        this.eventName = eventName;
        this.sex = sex;
        this.connectTime = connectTime;
        this.connectCnt = connectCnt;
        this.broadcastCreateYn = broadcastCreateYn;
        this.broadcastJoinYn = broadcastJoinYn;
        this.payYn = payYn;
    }

    private String eventName;
    private String sex;
    private int age;
    private String connectTime;
    private String connectCnt;
    private String broadcastCreateYn;
    private String broadcastJoinYn;
    private String payYn;
}
