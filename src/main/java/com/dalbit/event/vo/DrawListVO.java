package com.dalbit.event.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DrawListVO {
/*
    private Integer autoNo = 0; //-- 경품 번호
    private Integer memNo  = 0; //-- 회원 번호
    private Integer bbopgiGiftNo = 0; //-- 경품 번호
    private Integer bbopgiGiftPosNo = 0; //-- 경품 위치번호
    private Date insDate; //-- 등록일자
*/


    private Integer auto_no = 0; //-- 경품 번호
    private String mem_no  = ""; //-- 회원 번호
    private Integer bbopgi_gift_no = 0; //-- 경품 번호
    private Integer bbopgi_gift_pos_no = 0; //-- 경품 위치번호
    private Date ins_date; //-- 등록일자
}
