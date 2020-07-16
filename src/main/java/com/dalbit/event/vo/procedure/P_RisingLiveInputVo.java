package com.dalbit.event.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RisingLiveInputVo {
    /* Input */
    private String mem_no;                  // 요청 회원번호
    private Integer slct_type;              // 랭킹구분(1: 스타, 2: 팬)

}
