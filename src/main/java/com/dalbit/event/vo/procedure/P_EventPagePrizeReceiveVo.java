package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.EventPagePrizeReceiveVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_EventPagePrizeReceiveVo extends P_ApiVo {

    public P_EventPagePrizeReceiveVo(EventPagePrizeReceiveVo eventPagePrizeReceiveVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setEventIdx(eventPagePrizeReceiveVo.getEventIdx());
        setPrizeIdx(eventPagePrizeReceiveVo.getPrizeIdx());
        setReceiveWay(eventPagePrizeReceiveVo.getReceiveWay());
    }
    /* Input */
    private String mem_no;                  // 요청 회원번호

    private int eventIdx;
    private int prizeIdx;
    private int receiveWay;                 // 수령방법(1:경품받기, 2:달로받기)
}
