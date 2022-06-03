package com.dalbit.rank.vo;

import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainRankingPageVo;
import com.dalbit.main.vo.procedure.P_MainTimeRankingPageVo;
import com.dalbit.rank.vo.procedure.P_TimeRankListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class TimeRankListOutVO {

    private int rank;
    private String upDown;
    private String memNo;
    private String nickNm;
    private String memId;
    private String gender;
    private ImageVo profImg;
    private int level;
    private String grade;
    private int exp;
    private boolean isSpecial;
    private boolean isConDj;
    private int badgeSpecial;
    private int djPoint;
    private int listenerPoint;
    private int goodPoint;
    private int broadcastPoint;
    private int giftPoint;
    private int fanPoint;
    private int listenPoint;
    private String holder;
    private String roomNo;
    private int starCnt = 0;
    private int liveDjRank;
    private int liveFanRank;
    private int liveTime;
    private List<FanBadgeVo> liveBadgeList = new ArrayList<>();
    private String listenRoomNo;
    private int listenOpen;
    public TimeRankListOutVO(){}
    public TimeRankListOutVO(P_TimeRankListVo target) {
        setRank(target.getRank());
        setUpDown(target.getUp_down());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setMemId(target.getMemId());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLevel(target.getLevel());
        setGrade(target.getGrade());
        this.isSpecial = target.getSpecialdj_badge() > 0;
        this.isConDj = target.getIsConDj() > 0;
        this.badgeSpecial = target.getSpecialdj_badge();
        setDjPoint(target.getDjPoint());
        setListenerPoint(target.getListenerPoint());
        setListenPoint(target.getListenPoint());
        setGoodPoint(target.getGoodPoint());
        setBroadcastPoint(target.getBroadcastPoint());
        setGiftPoint(target.getGiftPoint());
        setFanPoint(target.getFanPoint());
        this.holder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", this.level + "");
        setRoomNo(target.getRoomNo());
        setStarCnt(target.getStarCnt());
        setLiveDjRank(target.getLiveDjRank() > 100 ? 0 : target.getLiveDjRank());
        setLiveFanRank(target.getLiveFanRank());
        setListenOpen(target.getListenOpen());
        setListenRoomNo(target.getListenRoomNo());
        setLiveTime(target.getLiveTime());
    }
}