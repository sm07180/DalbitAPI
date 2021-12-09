package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuPocketPageInputVo {
    public GganbuPocketPageInputVo() {}
    public GganbuPocketPageInputVo(String gganbuNo, String memNo) {
        this.gganbuNo = gganbuNo;
        this.memNo = memNo;
    }
    private String gganbuNo;
    private String memNo;
}
