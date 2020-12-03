package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class P_SleepVo extends P_ApiVo {
    private String mem_no;
    private String op_mem_no;
    private int type;
}
