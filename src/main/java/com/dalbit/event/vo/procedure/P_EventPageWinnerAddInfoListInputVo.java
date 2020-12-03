package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.EventPageWinnerAddInfoListVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;

@Getter
@Setter
public class P_EventPageWinnerAddInfoListInputVo extends P_ApiVo {

    public P_EventPageWinnerAddInfoListInputVo(EventPageWinnerAddInfoListVo eventPageWinnerAddInfoListVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setEventIdx(eventPageWinnerAddInfoListVo.getEventIdx());
        setPrizeIdx(eventPageWinnerAddInfoListVo.getPrizeIdx());
    }
    /* Input */
    private String mem_no;                  // 요청 회원번호

    private int eventIdx;
    private int prizeIdx;
}
