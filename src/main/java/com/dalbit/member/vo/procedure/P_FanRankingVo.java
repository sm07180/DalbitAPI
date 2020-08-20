package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanRankingVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_FanRankingVo {

    public P_FanRankingVo(){}
    public P_FanRankingVo(FanRankingVo fanRankingVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(fanRankingVo.getPage()) ? 1 : fanRankingVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(fanRankingVo.getRecords()) ? 10 : fanRankingVo.getRecords();

        setMem_no(new MemberVo().getMyMemNo(request));
        setStar_mem_no(fanRankingVo.getMemNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
        setRank_slct((DalbitUtil.isEmpty(fanRankingVo.getRankSlct()) || fanRankingVo.getRankSlct() == 0) ? 1 : fanRankingVo.getRankSlct());
        setRank_type((DalbitUtil.isEmpty(fanRankingVo.getRankType()) || fanRankingVo.getRankType() == 0) ? 1 : fanRankingVo.getRankType());
    }


    /* Input */
    private String mem_no;
    private String star_mem_no;
    private int pageNo;
    private int pageCnt;
    private int rank_slct;
    private int rank_type;

    /* Output */
    private String nickName;
    private String memSex;
    private String profileImage;
    private int enableFan;
    private int giftDal;
    private int giftedByeol;
}
