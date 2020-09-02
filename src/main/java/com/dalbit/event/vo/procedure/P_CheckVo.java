package com.dalbit.event.vo.procedure;

import com.dalbit.common.code.EventCode;
import com.dalbit.event.vo.request.CheckVo;
import com.dalbit.member.vo.MemberVo;

import javax.servlet.http.HttpServletRequest;

public class P_CheckVo {
    private String mem_no;
    private int event_idx;
    private boolean isMulti;

    public P_CheckVo(CheckVo checkVo, HttpServletRequest request){
        this.event_idx = checkVo.getEventIdx();
        this.mem_no = MemberVo.getMyMemNo(request);
        for(EventCode e : EventCode.values()){
            if(e.getEventIdx() == this.event_idx){
                this.isMulti = e.isMulti();
                break;
            }
        }
    }
}
