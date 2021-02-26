package com.dalbit.event.vo.procedure;

import com.dalbit.event.vo.request.ChampionshipVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ChampionshipVo {
    public P_ChampionshipVo(){}
    public P_ChampionshipVo(ChampionshipVo championshipVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(championshipVo.getSlctType());
        //setEvent_no(championshipVo.getEventNo());
    }

    /* Input */
    private String mem_no;
    private int slct_type;
    //private int event_no;

    /* Output */
    private int rank;
    private int level;
    private String grade;
    private String nickName;
    private String memSex;
    private String profileImage;
    private int totalPoint;
    private String qupid_mem_no;
    private String qupid_memSex;
    private String qupid_memNick;
    private String qupid_profileImage;
}
