package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum EventCode {
    인증샷(2, false, "2020.08 PC인증샷 이벤트"),
    노하우(3, true, "2020.09 방송 노하우 이벤트"),
    방송지원(4, false, "2020.09 방송 지원 이벤트"),
    웹캠(10, false, "2021.01 웹캠 지원 이벤트"),

    ;
    final private int eventIdx;
    final private boolean isMulti;  //중복참여 여부 true:중복참여, false:한번만 참여
    final private String desc;

    EventCode(int eventIdx, boolean isMulti, String desc){
        this.eventIdx = eventIdx;
        this.isMulti = isMulti;
        this.desc = desc;
    }
}
