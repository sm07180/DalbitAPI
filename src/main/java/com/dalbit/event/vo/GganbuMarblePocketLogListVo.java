package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GganbuMarblePocketLogListVo {
    private String gganbu_no;
    private String mem_no;
    private String mem_nick;
    private int marble_pocket_pt;
    private Date ins_date;
    private String ins_slct; // 구슬주머니 획득구분[e:구슬교환, u: 1만개 사용누적, s:1만개 선물 누적]
    private String chng_slct; // s:획득 , u:사용
    private String marble_pocket_cnt; // 주머니 수
    private String rcvReason; // 받은 사유
    private int exc_marble_cnt; // 교환 구슬 수
}
