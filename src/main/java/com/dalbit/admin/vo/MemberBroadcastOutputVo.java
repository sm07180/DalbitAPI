package com.dalbit.admin.vo;

import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class MemberBroadcastOutputVo extends SearchVo {

    private String subject_type;
    private String title;
    private Date start_date;
    private String startDateFormat;
    private Date end_date;
    private String endDateFormat;
    private String airtime;
    private int listenerCnt;
    private int managerCnt;
    private int giftCnt;
    private int byeolCnt;
    private int goodCnt;
    private int boosterCnt;
    private int os_type;
    private int inner;
}
