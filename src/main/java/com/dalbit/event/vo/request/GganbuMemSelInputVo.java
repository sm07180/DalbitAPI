package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMemSelInputVo {
    public GganbuMemSelInputVo() {}
    public GganbuMemSelInputVo(String gganbuNo, String memNo) {
        this.gganbuNo = gganbuNo;
        this.memNo = memNo;
    }
    private String gganbuNo; // 회차번호
    private String memNo; // 회원번호
}
