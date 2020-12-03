package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_LoginTotalOutVo extends P_ApiVo {
    private int sum_utotalCnt;
    private int sum_umaleCnt;
    private int sum_ufemaleCnt;
    private int sum_unoneCnt;
}
