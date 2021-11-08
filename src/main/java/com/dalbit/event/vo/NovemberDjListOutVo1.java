package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NovemberDjListOutVo1 {
    private String mem_no; // 회원번호
    private int rcv_dal_cnt; // 받은 달
    private int play_time; // 방송시간(초)
    private int rcv_bonus_gold; // 추가 적립금 (이벤트)
    private int rcv_booster; // 이벤트로 받은 부스터 수 (20개 한번만)
}
