package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RankingResultVo {

    private String memNo;                  //회원번호
    @NotNull
    private Integer slctType;              //랭킹구분(1: 경험치, 2: 좋아요, 3: 선물 )
    @NotNull
    private Integer round;                  //회차(1, 2, 3 )
}
