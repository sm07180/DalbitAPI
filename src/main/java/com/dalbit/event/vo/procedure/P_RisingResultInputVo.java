package com.dalbit.event.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RisingResultInputVo {
    /* Input */
    private String mem_no;                  // 요청 회원번호
    private Integer slct_type;              // 랭킹구분(1: 스타, 2: 팬)
    private Integer round;                  // 회차(1,2)
}
