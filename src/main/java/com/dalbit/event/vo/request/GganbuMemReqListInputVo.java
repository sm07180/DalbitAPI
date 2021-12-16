package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMemReqListInputVo {
    private String insSlct; // 신청구분 [r:받은신청, m:나의신청]
    private String gganbuNo; // 회차번호
    private String memNo; // 회원번호
    private int pageNo;
    private int pagePerCnt;
}
