package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class P_LoginTotalInPutVo extends P_ApiVo {

    private String dateList;

    private int slctType;
    private String startDate;
    private String endDate;

}
