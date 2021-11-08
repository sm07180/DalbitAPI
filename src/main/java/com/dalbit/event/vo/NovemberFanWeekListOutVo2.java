package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class NovemberFanWeekListOutVo2 {
    private String evt_no; // 회차
    private String fan_week_gift_no; // 경품 번호
    private String fan_week_gift_name; // 경품 이름
    private String fan_week_gift_file_name; // 경품 사진
    private int fan_week_gift_cnt; // 경품 수
    private int con_ins_cnt; // 응모조건수
    private int tot_ins_cnt; // 응모조건 만족인원수
    private Date ins_date; // 등록일자
    private Date upd_date; // 수정일자
    private String presentConditionImg = ""; // 참여 여부 이미지
}
