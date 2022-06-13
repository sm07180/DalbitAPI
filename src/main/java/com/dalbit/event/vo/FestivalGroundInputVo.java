package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FestivalGroundInputVo {
    private int seqNo;
    private String memNo;
    private int pageNo = 1;
    private int pagePerCnt = 30;
}
