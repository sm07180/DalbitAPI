package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class KeyboardBonusVO {
    private String the_date;  //DATE-- 집계일자
    private String mem_no;  //BIGINT-- 회원번호(BJ)
    private String mem_userid;  //VARCHAR(20)-- 회원아이디(BJ)
    private String mem_nick;  //VARCHAR(20)-- 회원대화명(BJ)
    private Integer play_time = 0;  //INT-- 방송시간합계 (초)
    private String one_time_yn = "n";  //CHAR(1)-- 방송시간 조건만족여부 (1단계)
    private String two_time_yn = "n";  //CHAR(1)-- 방송시간 조건만족여부 (2단계)
    private String one_step_rcv_yn = "n";  //CHAR(1)-- 보상수령여부 (1단계)
    private String one_step_rcv_date; //DATETIME-- 보상수령일자 (1단계)
    private String two_step_rcv_yn = "n";  //CHAR(1)-- 보상수령여부 (2단계)
    private String two_step_rcv_date; //DATETIME-- 보상수령일자 (2단계)
    private String ins_date;  //DATETIME-- 등록일자
    private String upd_date;  //DATETIME-- 수정일자
}
