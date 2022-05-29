package com.dalbit.rank.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rank.vo.TimeRankListDTO;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_TimeRankListVo extends P_ApiVo {

    public P_TimeRankListVo(){}
    public P_TimeRankListVo(TimeRankListDTO timeRankListDTO, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(timeRankListDTO.getPage()) ? 1 : timeRankListDTO.getPage();
        int pageCnt = DalbitUtil.isEmpty(timeRankListDTO.getRecords()) ? 10 : timeRankListDTO.getRecords();

        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setRanking_slct(timeRankListDTO.getRankSlct());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
        setRank_date(DalbitUtil.isEmpty(timeRankListDTO.getRankingDate()) ? DalbitUtil.getDate("yyyy-MM-dd HH:mm:ss") : timeRankListDTO.getRankingDate());
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
    private int isConDj;
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

}
