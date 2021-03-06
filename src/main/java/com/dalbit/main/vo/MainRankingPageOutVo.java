package com.dalbit.main.vo;

import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainRankingPageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MainRankingPageOutVo {

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
    private String djMemNo;
    private String djNickNm;
    private int djGoodPoint;
    private ImageVo djProfImg;
    private List<FanBadgeVo> liveBadgeList = new ArrayList<>();
    private String listenRoomNo;
    private Boolean isConDj;
    private int listenOpen;

    public MainRankingPageOutVo(){}
    public MainRankingPageOutVo(P_MainRankingPageVo target, boolean isAdmin) {
        setRank(target.getRank());
        setUpDown(target.getUp_down());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setMemId(target.getMemId());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLevel(target.getLevel());
        setGrade(target.getGrade());
        setExp(target.getExp());
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
        setListenPoint(target.getListenPoint());
        this.holder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", this.level + "");
        setRoomNo(target.getRoomNo());
        setStarCnt(target.getStarCnt());
        setLiveDjRank(target.getLiveDjRank() > 100 ? 0 : target.getLiveDjRank());
        setLiveFanRank(target.getLiveFanRank());
        setDjMemNo(target.getDj_mem_no());
        setDjNickNm(target.getDj_nickName());
        setDjGoodPoint(target.getDj_goodPoint());
        setDjProfImg(new ImageVo(target.getDj_profileImage(), target.getDj_memSex(), DalbitUtil.getProperty("server.photo.url")));
        setDjNickNm(target.getDj_nickName());
        if(!DalbitUtil.isEmpty(target.getLiveBadgeText()) && !DalbitUtil.isEmpty(target.getLiveBadgeIcon())){
            this.liveBadgeList.add(new FanBadgeVo(target.getLiveBadgeText(), target.getLiveBadgeIcon(), target.getLiveBadgeStartColor(), target.getLiveBadgeEndColor(), target.getLiveBadgeImage(), target.getLiveBadgeImageSmall()));
        }
//        setListenRoomNo(DalbitUtil.getListenRoomNo(target.getListenRoomNo(), target.getListenOpen(), isAdmin));
        setListenRoomNo(target.getListenRoomNo());
        setListenOpen(target.getListenOpen());
    }
}
