package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.MainRankRewardVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MainRankRewardVo {

    public P_MainRankRewardVo(){}
    public P_MainRankRewardVo(MainRankRewardVo mainRankRewardVo, HttpServletRequest request){

        setMem_no(MemberVo.getMyMemNo(request));
        setRanking_slct(mainRankRewardVo.getRankSlct());
        setRanking_type(mainRankRewardVo.getRankType());

    }

    /* Input */
    private String mem_no;
    private Integer ranking_slct;
    private Integer ranking_type;

}
