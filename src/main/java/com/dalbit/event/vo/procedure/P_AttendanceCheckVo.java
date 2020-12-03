package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_AttendanceCheckVo extends P_ApiVo {

    public P_AttendanceCheckVo(){}

    public P_AttendanceCheckVo(HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
    }

    private String mem_no;
}
