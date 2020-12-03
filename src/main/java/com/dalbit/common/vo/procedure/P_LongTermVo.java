package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_LongTermVo extends P_ApiVo {
    public P_LongTermVo(){}
    public P_LongTermVo(HttpServletRequest request){
        setMem_no(DalbitUtil.isEmpty(request.getParameter("memNo")) ? "" : request.getParameter("memNo"));
    }

    private String mem_no;
}
