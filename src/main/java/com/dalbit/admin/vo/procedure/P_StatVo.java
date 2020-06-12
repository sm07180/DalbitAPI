package com.dalbit.admin.vo.procedure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_StatVo {
    private int slctType;
    private String startDate;
    private String endDate;
    private int pageNo;
    private int pageCnt;
}
