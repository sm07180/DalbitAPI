package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class EventBasicVo {

    private String idx;
    private String title;
    private int always_yn;
    private Date start_date;
    private Date end_date;
    private int state;
    private int view_yn;
    private int prize_slct;
    private int add_info_slct;
    private String etc_url;
    private String pc_link_url;
    private String mobile_link_url;
    private String list_img_url;
    private String desc;
    private int prize_winner;
    private int winner_open;
    private Date announcement_date;
    private String winner_contents;
    private int fold_yn;
    private String notice;
    private int delete_yn;
    private Date reg_date;
    private String reg_op_name;
    private Date last_upd_date;
    private String last_op_name;
}
