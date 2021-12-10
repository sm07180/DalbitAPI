package com.dalbit.event.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DrawGiftVO {
/*
    private Integer bbopgiGiftNo; //-- 경품 번호
    private Integer bbopgiGiftName; //-- 경품 이름
    private Integer giftCnt; //-- 경품 수
    private Integer totInsCnt; //-- 총 회원이 사용한 응모받은수
    private Integer useCouponCnt; // 내 응모수
    private Date insDate; //-- 등록일자
    private Date updDate; //-- 수정일자
*/

    private Integer bbopgi_gift_no = 0; //-- 경품 번호
    private String bbopgi_gift_name; //-- 경품 이름
    private Integer gift_cnt = 0; //-- 경품 수
    private Integer tot_ins_cnt = 0; //-- 총 회원이 사용한 응모받은수
    private Integer use_coupon_cnt = 0; // 내 응모수
    private Integer temp_result_cnt = 0;
    private Date ins_date; //-- 등록일자
    private Date upd_date; //-- 수정일자
}
