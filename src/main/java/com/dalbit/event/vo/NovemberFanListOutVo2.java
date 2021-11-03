package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class NovemberFanListOutVo2 {
    private String auto_no; // 경품 번호
    private String fan_gift_name; // 경품 이름
    private String fan_gift_file_name; // 경품 사진
    private String fan_gift_cnt_file_name; // 경품 수 (이미지)
    private Date ins_date; // 등록일자
    private Date upd_date; // 수정일자
    private int gift_cnt; // 경품 수
    private int tot_ins_cnt; // 응모 받은 수
    private int fan_use_coupon_cnt; // 경품별 응모한 횟수
}
