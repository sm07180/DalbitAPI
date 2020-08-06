package com.dalbit.admin.vo.procedure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_LoginTotalOutVo {
    private int sum_utotalCnt;
    private int sum_umaleCnt;
    private int sum_ufemaleCnt;
    private int sum_unoneCnt;
}
