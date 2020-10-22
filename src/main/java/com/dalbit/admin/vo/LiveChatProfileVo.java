package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LiveChatProfileVo extends AdminBaseVo{

    private String mem_no;
    private String mem_userid;
    private String mem_nick;
    private String image_profile;
    private String mem_sex;
    private String room_no;
    private int state;
    private int auth;
}
