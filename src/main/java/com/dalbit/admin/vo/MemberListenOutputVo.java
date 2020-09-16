package com.dalbit.admin.vo;

import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class MemberListenOutputVo extends SearchVo {

    //output
    private String dj_mem_no;
    private String dj_userId;
    private String dj_nickName;
    private String dj_mem_sex;
    private String dj_birth_year;
    private String dj_birth_month;
    private String dj_birth_day;
    private String room_no;
    private String subject_type;
    private String title;
    private Date start_date;
    private String startDateFormat;
    private Date end_date;
    private String endDateFormat;
    private String listentime;
    private String forcedLeave;
    private int giftByeol;
    private int goodCnt;
    private int boosterCnt;
    private int inner;

    private String mem_nick;

    //summary
    private int totalforcedCnt;
    private int totalByeol;
    private int totalGood;
    private int totalBooster;

    public void setStart_date(Date start_date){
        this.start_date = start_date;
        this.startDateFormat = DalbitUtil.convertDateFormat(start_date, "yyyy.MM.dd HH:mm:ss");
    }
    public void setEnd_date(Date end_date){
        this.end_date = end_date;
        this.endDateFormat = DalbitUtil.convertDateFormat(end_date, "yyyy.MM.dd HH:mm:ss");
    }
}
