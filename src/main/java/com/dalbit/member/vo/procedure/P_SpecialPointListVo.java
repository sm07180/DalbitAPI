package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.request.SpecialPointListVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_SpecialPointListVo extends P_ApiVo {

    public P_SpecialPointListVo(){}
    public P_SpecialPointListVo(SpecialPointListVo specialPointListVo){
        setMem_no(specialPointListVo.getMemNo());
    }

    /* Input */
    private String mem_no;

    /* Output */
    private String ranking_date;
    private int rank;
    private double addPoint;
    private int timeRound;          //타임회차 (0: 일간, 1,2,3 회차)

}
