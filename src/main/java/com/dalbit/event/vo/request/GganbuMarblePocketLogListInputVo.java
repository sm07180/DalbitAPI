package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMarblePocketLogListInputVo {
    public GganbuMarblePocketLogListInputVo() {}
    public GganbuMarblePocketLogListInputVo(String gganbuNo, String memNo, String tabSlct) {
        this.gganbuNo = gganbuNo;
        this.memNo = memNo;
        this.tabSlct = tabSlct;
    }

    private String gganbuNo;
    private String memNo;
    private int pageNo = 1;
    private int pagePerCnt = 50;
    private String tabSlct = "r";
}
