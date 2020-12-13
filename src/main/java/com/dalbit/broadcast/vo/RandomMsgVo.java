package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RandomMsgVo {
    private int type;
    private String btnTitle;
    private String msg;
    private int conditionTime;
    private int runningTime;
}
