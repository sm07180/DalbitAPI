package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuRankListInputVo {
    private String gganbuNo;
    private int pageNo;
    private int pagePerCnt;

    private String memNo;
}
