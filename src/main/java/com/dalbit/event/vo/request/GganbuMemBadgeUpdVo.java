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
    private String badgeSlct = "p"; // 배지구분 [p:주머니, r:깐부신청]
}
