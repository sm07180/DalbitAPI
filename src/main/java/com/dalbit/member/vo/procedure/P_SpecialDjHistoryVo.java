package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.SpecialDjHistoryVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_SpecialDjHistoryVo {

    public P_SpecialDjHistoryVo(){}
    public P_SpecialDjHistoryVo(SpecialDjHistoryVo specialDjHistoryVo){
        setMem_no(specialDjHistoryVo.getMemNo());
    }

    private String mem_no;
    private String year;
    private String month;
    private String roundNo;
    private String memNick;
}
