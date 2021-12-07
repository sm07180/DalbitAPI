package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMemBadgeUpdVo {
    public GganbuMemBadgeUpdVo() {}
    public GganbuMemBadgeUpdVo(String gganbuNo, String memNo) {
        this.gganbuNo = gganbuNo;
        this.memNo =memNo;
    }
    private String gganbuNo;
    private String memNo;
}
