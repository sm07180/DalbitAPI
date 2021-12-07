package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GganbuMemMarbleInsInputVo {
    public GganbuMemMarbleInsInputVo () {}
    public GganbuMemMarbleInsInputVo(String memNo, String insSlct, String roomNo, int rMarbleCnt, int yMarbleCnt, int bMarbleCnt, int vMarbleCnt) {
        this.memNo = memNo;
        this.insSlct = insSlct;
        this.roomNo = roomNo;
        this.rMarbleCnt = rMarbleCnt;
        this.yMarbleCnt = yMarbleCnt;
        this.bMarbleCnt = bMarbleCnt;
        this.vMarbleCnt = vMarbleCnt;
    }
    private String memNo; // 회원번호
    private String insSlct; // 획득구분 [r:방송, c:달충전, e:교환, b:배팅]
    private String roomNo = "0"; // 룸번호[획득구분 r에서 사용]
    private int rMarbleCnt = 0; // 빨강구슬 획득수
    private int yMarbleCnt = 0; // 노랑구슬 획득수
    private int bMarbleCnt = 0; // 파랑구슬 획득수
    private int vMarbleCnt = 0; // 보라구슬 획득수

    private int marbleCnt = 0; // 획득한 구슬 개수
    private String winSlct; // 배팅시 승패여부 [w: 승, l: 패]
    private String bettingSlct; // 배팅 구분[a:홀, b:짝]

    private int s_return = 0;
}
