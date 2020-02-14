package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectRoomVo {
    private String room_no;
    private int auth;
    private String device_id;
    private int state;
}
