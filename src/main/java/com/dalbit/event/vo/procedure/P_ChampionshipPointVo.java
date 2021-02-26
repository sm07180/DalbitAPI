package com.dalbit.event.vo.procedure;

import com.dalbit.event.vo.request.ChampionshipPointVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ChampionshipPointVo {
    public P_ChampionshipPointVo(){}
    public P_ChampionshipPointVo(ChampionshipPointVo championshipPointVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(championshipPointVo.getSlctType());
        //setEvent_no(championshipPointVo.getEventNo());
    }

    /* Input */
    private String mem_no;
    private int slct_type;
    //private int event_no;

    /* Output */
    private int rank;
    private int level;
    private String grade;
    private String memNick;
    private int winPoint1;
    private int winPoint2;
    private int winPoint3;
    private int winPoint4;
    private int winPoint5;

}
