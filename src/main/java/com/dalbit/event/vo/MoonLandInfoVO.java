package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoonLandInfoVO {
    private Integer moon_no;//	    INT		    -- 회차번호
    private String start_date;//	    DATETIME	-- 시작일자
    private String end_date;//	    DATETIME	-- 종료일자
    private String ins_date;//	    DATETIME	-- 등록일자
}
