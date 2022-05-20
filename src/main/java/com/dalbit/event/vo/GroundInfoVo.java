package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroundInfoVo {
    private int ground_no;      // -- 회차번호
    private String start_date;  // -- 시작일자
    private String end_date;    // -- 종료일자
}
