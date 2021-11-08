package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NovemberFanListOutVo1 {
    private int coupon_cnt; // 사용 가능 응모권
    private int fan_use_coupon_cnt; // 총 응모 횟수
    private int fan_week_use_coupon_cnt; // 이번회 응모 횟수
}
