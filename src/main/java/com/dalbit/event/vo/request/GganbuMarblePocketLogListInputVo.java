package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMarblePocketLogListInputVo {
    public GganbuMarblePocketLogListInputVo() {}
    public GganbuMarblePocketLogListInputVo(String gganbuNo, String memNo) {
        this.gganbuNo = gganbuNo;
        this.memNo = memNo;
    }

    private String gganbuNo;
    private String memNo;
    private int pageNo = 1;
    private int pagePerCnt = 50;
}
