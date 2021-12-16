package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GganbuMemViewStatInsVo {
    private int s_return; // -2: 신청자 깐부없음, -1: 이벤트기간 아님, 0: 에러, 1:정상
    private int s_rcvCnt; // 지급할 구슬의수
}
