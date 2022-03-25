package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DallagersEventScheduleSelVo {
    private Integer cnt;                //INT		-- 로우 수
    private Integer seq_no;             //INT		-- 회차번호
    private String start_date;          //DATETIME	-- 시작일자
    private String end_date;            //DATETIME	-- 종료일자
}
