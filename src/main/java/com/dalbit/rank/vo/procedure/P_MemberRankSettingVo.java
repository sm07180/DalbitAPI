package com.dalbit.rank.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rank.vo.RankSettingVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MemberRankSettingVo extends P_ApiVo {
    public P_MemberRankSettingVo(){}
    public P_MemberRankSettingVo(RankSettingVO rankSettingVO, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setApply_ranking("1".equals(rankSettingVO.getIsRankData()) || "TRUE".equals(rankSettingVO.getIsRankData().toUpperCase()) ? 1 : 0);

    }

    private String mem_no;
    private int apply_ranking;
}
