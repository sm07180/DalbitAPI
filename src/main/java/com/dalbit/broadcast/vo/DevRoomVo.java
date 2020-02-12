package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DevRoomVo {
    private String roomNo;
    private String title;
    private String startDt;
    private int auth;
}
