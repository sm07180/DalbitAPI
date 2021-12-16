package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuRoundInfoOutputVo {
    public GganbuRoundInfoOutputVo() {}
    public GganbuRoundInfoOutputVo(String gganbu_no, String start_date, String end_date, String ins_date) {
        this.gganbuNo = gganbu_no;
        this.startDate = start_date;
        this.endDate = end_date;
        this.insDate = ins_date;
    }
    private String gganbuNo; // 회차번호
    private String startDate; // 시작일자
    private String endDate; // 종료일자
    private String insDate; // 등록일자
}
