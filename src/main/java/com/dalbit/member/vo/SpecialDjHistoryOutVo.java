package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_SpecialDjHistoryVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SpecialDjHistoryOutVo {

    public SpecialDjHistoryOutVo(P_SpecialDjHistoryVo pSpecialDjHistoryVo){
        setSelectionDate(pSpecialDjHistoryVo.getYear()+"년 "+pSpecialDjHistoryVo.getMonth()+"월");
        setRoundNo(pSpecialDjHistoryVo.getRoundNo()+"기");
    }

    private String selectionDate;
    private String roundNo;
}
