package com.dalbit.event.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RankingLiveInputVo {

    /* Input */
    private String mem_no;                  //방리스트 요청 회원번호
    private Integer slct_type;               //랭킹구분(1: 경험치, 2: 좋아요, 3: 선물 )

}
