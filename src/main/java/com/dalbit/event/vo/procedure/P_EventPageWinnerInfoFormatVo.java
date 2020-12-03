package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class P_EventPageWinnerInfoFormatVo extends P_ApiVo {
    public P_EventPageWinnerInfoFormatVo() {
        setMem_no(new MemberVo().getMyMemNo());
    }
    /* Input */
    private String mem_no;                  // 요청 회원번호

    /* Output */
    private String mem_name;
    private String mem_phone;
}
