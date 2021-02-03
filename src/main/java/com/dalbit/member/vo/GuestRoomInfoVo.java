package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class GuestRoomInfoVo {
    private String roomNo;
    private int guestYn;
    private String bjMemNo;
    private String memNick;
    private String gender;
    private String profImg;

}
