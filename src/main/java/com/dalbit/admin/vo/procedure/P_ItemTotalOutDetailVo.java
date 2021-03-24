package com.dalbit.admin.vo.procedure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_ItemTotalOutDetailVo {

    private int monthly;
    private String daily;
    private String date;
    private String hour;
    private int the_hr;
    private String year;
    private String the_date;
    private String start_date;
    private String end_date;
    private String weekNo;
    private int month;
    private int the_day;
    private long changeCnt;
    private long changeAmt;
    private long dalgiftCnt;
    private long dalgiftAmt;
    private long subsCnt;
    private long subsAmt;
    private long broadgiftCnt;
    private long broadgiftAmt;
    private long castgiftCnt;
    private long castgiftAmt;

    private long mailboxCnt;
    private long mailboxAmt;
    private long totalCnt;
    private long totalCntAccum;
    private long totalAmt;
    private long totalAmtAccum;

}



