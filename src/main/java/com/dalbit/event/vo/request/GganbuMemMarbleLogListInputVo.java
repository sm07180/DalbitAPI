package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMemMarbleLogListInputVo {
    private String gganbuNo; // 회차번호
    private String memNo; // 회원번호
    private int pageNo; // 페이지 번호
    private int pagePerCnt; // 페이지 당 노출 건수 (Limit)
}
