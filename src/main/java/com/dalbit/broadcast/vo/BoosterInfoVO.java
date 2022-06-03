package com.dalbit.broadcast.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BoosterInfoVO {
    private Integer auto_no; //     INT(11) 	 -- 자동증가번호
    private String mem_no; //      BIGINT(20)   -- 회원번호
    private String room_no; //     BIGINT(20)   -- 방번호
    private String dj_mem_no; //   BIGINT(20)   -- 방장 회원번호
    private Integer item_cnt; //    INT(11)      -- 아이템수
    private Integer item_point; //  INT(11)      -- 아이템점수
    private Integer accum_cnt; //   INT(11)      -- 누적사용수
    private Date start_date; //  DATETIME(6)  -- 시작일자
    private Date end_date; //    DATETIME(6)  -- 종료일자
}
