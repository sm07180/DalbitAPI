package com.dalbit.event.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DrawTicketVO {
/*    private Integer memNo = 0; //	-- 회원 번호
    private Integer buyDalCnt = 0; //-- 이벤트 기간동안 충전한 달
    private Integer cencelDalCnt = 0; //-- 이벤트 기간동안 충전취소 달
    private Integer insCouponCnt = 0; //-- 받은 쿠폰수
    private Integer useCouponCnt = 0; //-- 사용 쿠폰수
    private Integer couponCnt = 0; //-- 현재 쿠폰수
    private Date insDate; //-- 등록일자
    private Date updDate; //-- 수정일자*/


    private String mem_no = ""; //	-- 회원 번호
    private Integer buy_dal_cnt = 0; //-- 이벤트 기간동안 충전한 달
    private Integer cencel_dal_cnt = 0; //-- 이벤트 기간동안 충전취소 달
    private Integer ins_coupon_cnt = 0; //-- 받은 쿠폰수
    private Integer use_coupon_cnt = 0; //-- 사용 쿠폰수
    private Integer coupon_cnt = 0; //-- 현재 쿠폰수
    private Date ins_date; //-- 등록일자
    private Date upd_date; //-- 수정일자
}
