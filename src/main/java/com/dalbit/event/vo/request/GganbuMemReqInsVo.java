package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GganbuMemReqInsVo {
    private String gganbuNo; // 회차번호
    private String memNo; // 회원번호(신청자)
    private String ptrMemNo; // 회원번호(대상자)
    private String memNick = "";
}
