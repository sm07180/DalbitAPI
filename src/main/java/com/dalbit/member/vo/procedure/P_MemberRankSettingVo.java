package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MemberRankSettingVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MemberRankSettingVo extends P_ApiVo {
    public P_MemberRankSettingVo(){}
    public P_MemberRankSettingVo(MemberRankSettingVo memberRankSettingVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setApply_ranking("1".equals(memberRankSettingVo.getIsRankData()) || "TRUE".equals(memberRankSettingVo.getIsRankData().toUpperCase()) ? 1 : 0);

    }

    private String mem_no;
    private int apply_ranking;
}
