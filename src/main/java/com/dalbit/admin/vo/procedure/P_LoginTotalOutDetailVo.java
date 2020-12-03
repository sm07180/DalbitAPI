package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_LoginTotalOutDetailVo extends P_ApiVo {

    private String date;
    private String year;
    private String month;
    private String day;
    private String hour;
    private int totalCnt;
    private int maleCnt;
    private int femaleCnt;
    private int noneCnt;

    private int bTotalCnt;
    private int bMaleCnt;
    private int bFemaleCnt;
    private int bNoneCnt;
}