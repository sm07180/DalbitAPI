package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GganbuMarblePocketOpenInsVo {
    private String memNo;
    private int marblePocketPt; // 주머니 점수
    private int averageLevel; // 평균 레벨
}
