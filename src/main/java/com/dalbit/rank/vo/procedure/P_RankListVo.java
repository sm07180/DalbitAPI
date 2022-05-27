package com.dalbit.rank.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rank.vo.RankListDTO;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_RankListVo extends P_ApiVo {

    public P_RankListVo() {
    }

    public P_RankListVo(RankListDTO rankListDTO, HttpServletRequest request) {
        int pageNo = DalbitUtil.isEmpty(rankListDTO.getPage()) ? 1 : rankListDTO.getPage();
        int pageCnt = DalbitUtil.isEmpty(rankListDTO.getRecords()) ? 10 : rankListDTO.getRecords();

        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(rankListDTO.getRankType());
        setRanking_slct(rankListDTO.getRankSlct());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
        setRankingDate(rankListDTO.getRankingDate());
        setPrevRankingDate(rankListDTO.getPrevRankingDate());
    }

    /* Input */
    private int memLogin;
    private String mem_no;
    private int slct_type;
    private int ranking_slct;
    private int pageNo;
    private int pageCnt;
    private String rankingDate;
    private String prevRankingDate;

    /* Output */
    private int rank;
    private String up_down;
    private String nickName;
    private String memId;
    private String memSex;
    private String profileImage;
    private int level;
    private String grade;
    private int exp;
    private int specialdj_badge;
    private int djPoint;
    private int listenerPoint;
    private int goodPoint;
    private int broadcastPoint;
    private int giftPoint;
    private int fanPoint;
    private int listenPoint;
    private String roomNo;
    private int starCnt;
    private int liveDjRank;
    private int liveFanRank;
    private String dj_mem_no;
    private String dj_nickName;
    private int dj_goodPoint;
    private String dj_profileImage;
    private String dj_memSex;
    private String listenRoomNo;
    private int listenOpen;
    private int isConDj;
}
