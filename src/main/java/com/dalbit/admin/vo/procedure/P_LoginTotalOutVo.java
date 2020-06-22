package com.dalbit.admin.vo.procedure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_LoginTotalOutVo {
    private int sum_totalCnt;
    private int sum_maleCnt;
    private int sum_femaleCnt;
    private int sum_noneCnt;

    private int sum_bTotalCnt;
    private int sum_bMaleCnt;
    private int sum_bFemaleCnt;
    private int sum_bNoneCnt;
}
