package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyboardWinVO {
    private String the_date;//DATE-- 집계일자
    private String the_seq;//TINYINT-- 회차
    private String mem_no;//BIGINT-- 회원번호
    private String mem_userid;//VARCHAR(20)-- 회원아이디
    private String mem_nick;//VARCHAR(20)-- 회원대화명
    private String pre_code;//CHAR(3)-- 보상코드
    private String rcv_yn;//CHAR(1)-- 보상수령여부
    private String code_name;//VARCHAR(30)-- 보상이름
    private String pre_slct;//CHAR(1)-- 보상구분[r:캐럿, k:현물]
    private String pre_cnt;//INT-- 보상갯수
    private String ins_date;//DATETIME-- 등록일자
}
