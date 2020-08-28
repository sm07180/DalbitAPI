package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class EventBasicVo {

    private int idx;
    private String event_title;
    private Date start_datetime;
    private Date end_datetime;
    private Date view_start_datetime;
    private Date view_end_datetime;
    private String platform;
    private int event_slct;
    private int is_pop;
    private int is_view;
    private int is_reply;
    private String pc_img_url;
    private String pc_link_url;
    private String mobile_img_url;
    private String mobile_link_url;
    private String thumb_img_url;
    private String desc;
    private Date reg_date;
    private String op_name;
    private Date last_upd_date;
    private String last_op_name;
}
