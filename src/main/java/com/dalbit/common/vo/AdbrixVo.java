package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdbrixVo {

    public AdbrixVo(){}

    private String sex;
    private String age;
    private String connectTime;
    private String connectCnt;
    private String broadcastCreateYn;
    private String broadcastJoinYn;
    private String payYn;
    private int korAge;
    private int realAge;
}
