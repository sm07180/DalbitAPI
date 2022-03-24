package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhatsUpRequestVo {
    private String memNo;               // 회원번호
    private String seqNo;               // 회차번호[1,2]
    private Integer pageNo;
    private Integer pagePerCnt;
}
