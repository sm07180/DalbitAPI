package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.AwardVoteVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_AwardVoteVo {
    public P_AwardVoteVo(){}
    public P_AwardVoteVo(AwardVoteVo awardVoteVo, HttpServletRequest request){
        setRevoteYn(!DalbitUtil.isEmpty(awardVoteVo.getIsRevote()) ? (("true".equals(awardVoteVo.getIsRevote().toLowerCase()) || "1".equals(awardVoteVo.getIsRevote())) ? 1 : 0) : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setSelectYear(awardVoteVo.getYear());
        setDj_idx1(awardVoteVo.getDjIdx_1());
        setDj_idx2(awardVoteVo.getDjIdx_2());
        setDj_idx3(awardVoteVo.getDjIdx_3());
        setDj_mem_no1(awardVoteVo.getDjMemNo_1());
        setDj_mem_no2(awardVoteVo.getDjMemNo_2());
        setDj_mem_no3(awardVoteVo.getDjMemNo_3());
    }
    private int revoteYn;
    private String mem_no;
    private String selectYear;
    private int dj_idx1;
    private int dj_idx2;
    private int dj_idx3;
    private String dj_mem_no1;
    private String dj_mem_no2;
    private String dj_mem_no3;
}
