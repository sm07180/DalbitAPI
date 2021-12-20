package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuBettingLogListInputVo {
    public GganbuBettingLogListInputVo() {}
    public GganbuBettingLogListInputVo(String gganbuNo, String memNo, int pageNo, int pagePerCnt) {
        this.gganbuNo = gganbuNo;
        this.memNo = memNo;
        this.pageNo = pageNo;
        this.pagePerCnt = pagePerCnt;
    }
    private String gganbuNo;
    int pageNo = 1;
    int pagePerCnt = 50;

    private String memNo;
}
