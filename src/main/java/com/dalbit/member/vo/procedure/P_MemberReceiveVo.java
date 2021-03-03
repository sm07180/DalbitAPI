package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MemberReceiveVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MemberReceiveVo extends P_ApiVo {
    public P_MemberReceiveVo(){}
    public P_MemberReceiveVo(MemberReceiveVo memberReceiveVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setMem_no_star(memberReceiveVo.getMemNo());
        setAlertYn("1".equals(memberReceiveVo.getIsReceive()) || "TRUE".equals(memberReceiveVo.getIsReceive().toUpperCase()) ? 1 : 0);
    }
    private String mem_no;
    private String mem_no_star;
    private int alertYn;
}
