package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_SpecialPointListVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialPointListOutVo {

    public SpecialPointListOutVo(P_SpecialPointListVo target){
        setRankDate(target.getRanking_date());
        setRank(target.getRank());
        setAddPoint(target.getAddPoint());
        setTimeRound(target.getTimeRound());
    }

    private String rankDate;
    private int rank;
    private double addPoint;
    private int timeRound;
}
