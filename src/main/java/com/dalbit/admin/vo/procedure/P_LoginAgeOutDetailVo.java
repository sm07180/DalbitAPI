package com.dalbit.admin.vo.procedure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_LoginAgeOutDetailVo {

    private String daily;
    private int monthly;
    private int month;
    private String date;
    private int day;
    private int hour;

    private int totalCnt;
    private int age10Cnt;
    private int age20Cnt;
    private int age30Cnt;
    private int age40Cnt;
    private int age50Cnt;
    private int age60Cnt;

}