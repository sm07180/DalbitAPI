package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class BroadcastDetailVo {

    private String room_no;
    private String title;
    private String srtDt;
    private String expectedEndDt;
    private String osType;
    private String roomBgImg;
    private String bjStreamId;
    private String nickNm;
    private String gender;
    private String profImg;
    private int entryCnt;
    private int linkCnt;
    private int goldCnt;

    private String bjPlayToken;
    private String antUrl;
    private String antAppName;

    private String wsUrl;
    private String applicationName;
    private String streamName;

    private String mem_no;
    private String mem_nick;


}
