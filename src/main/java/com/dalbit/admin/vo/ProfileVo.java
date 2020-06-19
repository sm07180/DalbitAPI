package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileVo extends AdminBaseVo{

    /* ProfileVo output */
    private String mem_no;
    private String last_upd_date;
    private String mem_join_date;
    private String image_profile;
    private String mem_nick;
    private String mem_id;
    private String mem_userid;
    private int reportedCnt;
    private String reportYn;

    private String room_no;
    private int state;
    private int auth;
}
