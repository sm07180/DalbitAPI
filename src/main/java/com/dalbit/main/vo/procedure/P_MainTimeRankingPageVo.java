package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.main.vo.request.MainRankingPageVo;
import com.dalbit.main.vo.request.MainTimeRankingPageVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MainTimeRankingPageVo extends P_ApiVo {

    public P_MainTimeRankingPageVo(){}
    public P_MainTimeRankingPageVo(MainTimeRankingPageVo mainTimeRankingPageVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mainTimeRankingPageVo.getPage()) ? 1 : mainTimeRankingPageVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mainTimeRankingPageVo.getRecords()) ? 10 : mainTimeRankingPageVo.getRecords();

        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setRanking_slct(mainTimeRankingPageVo.getRankSlct());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
        setRank_date(DalbitUtil.isEmpty(mainTimeRankingPageVo.getRankingDate()) ? DalbitUtil.getDate("yyyy-MM-dd HH:mm:ss") : mainTimeRankingPageVo.getRankingDate());
    }

    /* Input */
    private int memLogin;
    private String mem_no;
    private int ranking_slct;
    private int pageNo;
    private int pageCnt;
    private String rank_date;

    /* Output */
    private int rank;
    private int liveDjRank;
    private int liveFanRank;
    private String up_down;
    private String nickName;
    private String memId;
    private String memSex;
    private String profileImage;
    private int level;
    private String grade;
    private int specialdj_badge;
    private int djPoint;
    private int listenerPoint;
    private int goodPoint;
    private int liveTime;
    private int broadcastPoint;
    private int giftPoint;
    private int fanPoint;
    private int listenPoint;
    private String roomNo;
    private String listenRoomNo;
    private int listenOpen;
    private int starCnt;

    private String liveBadgeText;
    private String liveBadgeIcon;
    private String liveBadgeStartColor;
    private String liveBadgeEndColor;
    private String liveBadgeImage;
    private String liveBadgeImageSmall;

    /*private int exp;
    private String dj_mem_no;
    private String dj_nickName;
    private int dj_goodPoint;*/
}
