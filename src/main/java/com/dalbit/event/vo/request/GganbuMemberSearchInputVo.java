package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMemberSearchInputVo {
    private String memNo;
    private String searchText = "";
    private int pageNo = 1;
    private int pagePerCnt = 50;
    private int myLevel = 0;
}
