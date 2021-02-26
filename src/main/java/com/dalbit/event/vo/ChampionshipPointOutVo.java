package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.event.vo.procedure.P_ChampionshipPointVo;
import com.dalbit.event.vo.procedure.P_ChampionshipVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChampionshipPointOutVo {

    private int rank;
    private String memNo;
    private String nickNm;
    private int winPoint1;
    private int winPoint2;
    private int winPoint3;
    private int winPoint4;
    private int winPoint5;


    public ChampionshipPointOutVo(){}
    public ChampionshipPointOutVo(P_ChampionshipPointVo target){
        setRank(target.getRank());
        setMemNo(target.getMem_no());
        setNickNm(target.getMemNick());
        setWinPoint1(target.getWinPoint1());
        setWinPoint2(target.getWinPoint2());
        setWinPoint3(target.getWinPoint3());
        setWinPoint4(target.getWinPoint4());
        setWinPoint5(target.getWinPoint5());
    }
}
