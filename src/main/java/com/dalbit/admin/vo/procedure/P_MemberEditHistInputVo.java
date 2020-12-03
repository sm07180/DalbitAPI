package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberEditHistInputVo extends P_ApiVo {
    private String mem_no;
    private int pageNo;
    private int pageCnt;
}
