package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.EventPageWinVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_EventPageWinResultInputVo extends P_ApiVo {

    public P_EventPageWinResultInputVo(EventPageWinVo eventPageWinVo, HttpServletRequest request) {
        setMem_no(new MemberVo().getMyMemNo(request));
        setEventIdx(eventPageWinVo.getEventIdx());
    }
    /* Input */
    private String mem_no;                  // 요청 회원번호
    private int eventIdx;
}
