package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class GganbuFanListVo extends P_ApiVo {
    /* Input */
    private String memNo;
    private String ptrMemNo;
    private Integer ordSlct;
    private int pageNo = 1;
    private int pagePerCnt = 50;

    /* Output */
    private String nickName;
    private String memSex;
    private String profileImage;
    private int enableFan;

    private String fanMemo;
    private int listenTime;
    private int giftedByeol;
    private Date lastlistenDate;
    private Date regDate;

    private String rcvYn;
    private String sendYn;
    private String mem_no;
    private ImageVo fanMemProfile;
    private int averageLevel;
    private int memLevel;
    private int level;
}
