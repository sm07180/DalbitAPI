package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NovemberDjListOutVo3 {
    private String dj_gift_no; // 경품 번호
    private String dj_gift_name; // 경품 이름
    private String dj_gift_file_name; // 경품 파일이름
    private int dj_gift_dal_cnt; // 경품 달 수
    private int dj_gift_play_time; // 경품 방송시간
    private int dj_gift_price; // 경품 가격
    private Date ins_date; // 등록일자
    private Date upd_date; // 수정일자
}
