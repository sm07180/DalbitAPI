package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_EventPageListInputVo extends P_ApiVo {
    /* Input */
    private String mem_no;                  // 요청 회원번호

    private int pageNo;
    private int pageCnt;
}
