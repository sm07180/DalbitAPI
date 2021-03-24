package com.dalbit.admin.vo.procedure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_ItemTotalOutVo {
    private long sum_changeCnt;
    private long sum_changeAmt;
    private long sum_dalgiftCnt;
    private long sum_dalgiftAmt;
    private long sum_subsCnt;
    private long sum_subsAmt;
    private long sum_broadgiftCnt;
    private long sum_broadgiftAmt;
    private long sum_castgiftCnt;
    private long sum_castgiftAmt;
    private long sum_mailboxCnt;
    private long sum_mailboxAmt;
    private long sum_totalCnt;
    private long sum_totalCntAccum;
    private long sum_totalAmt;
    private long sum_totalAmtAccum;
}