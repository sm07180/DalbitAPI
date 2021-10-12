package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.main.vo.request.MainRankingPageVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MainRankingPageVo extends P_ApiVo {

    public P_MainRankingPageVo(){}
    public P_MainRankingPageVo(MainRankingPageVo mainRankingPageVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mainRankingPageVo.getPage()) ? 1 : mainRankingPageVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mainRankingPageVo.getRecords()) ? 10 : mainRankingPageVo.getRecords();

        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(mainRankingPageVo.getRankType());
        setRanking_slct(mainRankingPageVo.getRankSlct());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
        setRankingDate(mainRankingPageVo.getRankingDate());

    }

    /* Input */
    private int memLogin;
    private String mem_no;
    private int slct_type;
    private int ranking_slct;
    private int pageNo;
    private int pageCnt;
    private String rankingDate;

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

    private String liveBadgeText;
    private String liveBadgeIcon;
    private String liveBadgeStartColor;
    private String liveBadgeEndColor;
    private String liveBadgeImage;
    private String liveBadgeImageSmall;

    private String myLiveBadgeText;
    private String myLiveBadgeIcon;
    private String myLiveBadgeStartColor;
    private String myLiveBadgeEndColor;
    private String myLiveBadgeImage;
    private String myLiveBadgeImageSmall;
}
