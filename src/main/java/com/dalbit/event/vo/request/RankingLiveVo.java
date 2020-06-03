package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingLiveVo {

    private String memNo;                  //회원번호
    private Integer slctType;              //랭킹구분(1: 경험치, 2: 좋아요, 3: 선물 )

}
