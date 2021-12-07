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
}
