package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_GuestListVo {

    private String mem_no;
    private String image_profile;
    private String mem_sex;
    private String mem_nick;
    private String room_no;
    private int auth;

    private int guest_yn;
    private int guest_propose;
}
