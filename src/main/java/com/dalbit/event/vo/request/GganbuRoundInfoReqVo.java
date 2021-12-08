package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuRoundInfoReqVo {
    private String gganbu_no; // 회차번호
    private String start_date; // 시작일자
    private String end_date; // 종료일자
    private String ins_date; // 등록일자
}
