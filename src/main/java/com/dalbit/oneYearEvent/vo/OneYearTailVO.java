package com.dalbit.oneYearEvent.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OneYearTailVO {
    private String memNo;
    private int pageNo;
    private int pagePerCnt;

    private String tailNo;
    private String tailMemNo;
    private String tailMemId;
    private String tailMemSex;
    private String tailMemIp;
    private String tailConts;
    private String tailLoginMedia;
//    private String loginMedia;
//    private String memNick;
//    private String memUserid;
//    private String imageProfile;
//    private Date insDate;
}
