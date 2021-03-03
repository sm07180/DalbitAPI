package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.SpecialLeagueVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_SpecialLeagueVo extends P_ApiVo {
    public P_SpecialLeagueVo(){}
    public P_SpecialLeagueVo(SpecialLeagueVo specialLeagueVo, HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setRoundNo(specialLeagueVo.getRoundNo());
    }

    /* Input */
    private int memLogin;
    private String mem_no;
    private int roundNo;

    /* Output */
    private int rank;
    private String up_down;
    private String mem_nick;
    private String mem_sex;
    private String profileImage;
    private int giftPoint;
    private int listenerPoint;
    private int goodPoint;
    private String fanPoint;
    private int totalPoint;

}
