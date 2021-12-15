package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMarbleExchangeInputVo {
    public GganbuMarbleExchangeInputVo () {}
    public GganbuMarbleExchangeInputVo(String memNo, String ptrMemNo, String roomNo, int dalCnt) {
        this.memNo = memNo;
        this.ptrMemNo = ptrMemNo;
        this.roomNo = roomNo;
        this.dalCnt = dalCnt;
    }
    private String memNo; // 회원번호
    private String ptrMemNo; // dj 회원번호
    private String roomNo; // 룸번호
    private int dalCnt; // 달수
}
