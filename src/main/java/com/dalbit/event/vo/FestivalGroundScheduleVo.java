package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FestivalGroundScheduleVo {
    private int cnt;           //  랭킹
    private int seq_no;        //  회차 번호
    private String start_date; //  시작일자
    private String end_date;   //  종료일자
}
