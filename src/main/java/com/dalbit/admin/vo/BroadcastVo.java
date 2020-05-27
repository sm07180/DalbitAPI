package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BroadcastVo {
    private String room_no;
    private String image_background;
    private String mem_no;
    private String mem_nick;
    private String mem_id;
    private String subject_name;
    private String title;
    private int liveListener;
    private int reportedCnt;
    private String start_date;
    private int state;
}
