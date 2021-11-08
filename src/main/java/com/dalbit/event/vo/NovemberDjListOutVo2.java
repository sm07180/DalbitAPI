package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NovemberDjListOutVo2 {
    private String pro_dj_gift_no; // 경품 번호(조건 만족)
    private String pro_dj_gift_name; // 경품 이름 (조건 만족)
    private String pro_dj_gift_file_name; // 경품 파일이름(조건 만족)
    private int pro_dj_gift_dal_cnt; // 경품 달 수(조건 만족)
    private int pro_dj_gift_play_time; // 경품 방송시간 (조건 만족)
    private Date pro_ins_date; // 등록일자(조건 만족)
    private Date pro_upd_date; // 수정일자(조건 만족)
}
