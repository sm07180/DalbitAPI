package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventMemberVo {

    private int event_idx;
    private int event_member_idx;
    private String mem_no;
    private int platform;
    private int del_yn;
}
