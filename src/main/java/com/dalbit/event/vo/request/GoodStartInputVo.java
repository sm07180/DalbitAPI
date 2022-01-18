package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodStartInputVo {
    private String goodNo;
    private String memNo;
    private int pageNo = 1;
    private int pagePerCnt = 99999;
}
