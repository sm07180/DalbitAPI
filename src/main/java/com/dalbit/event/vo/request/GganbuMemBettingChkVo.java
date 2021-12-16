package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMemBettingChkVo {
    public GganbuMemBettingChkVo() {}
    public GganbuMemBettingChkVo(String gganbuNo, String memNo) {
        this.gganbuNo = gganbuNo;
        this.memNo = memNo;
    }
    private String gganbuNo;
    private String memNo;
}
