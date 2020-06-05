package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class LiveChatOutputVo {

    /* return result */
    private Date writeDate;
    private String writeDateFormat;
    private String mem_no;
    private String auth;
    private String nickname;
    private String msg;
    private String chatIdx;
    private String room_no;

    /* summry */
    private int chatCnt;
    private int djCnt;
    private int listenerCnt;
    private int managerCnt;
    private int forcedCnt;
    private int djOs;

}
