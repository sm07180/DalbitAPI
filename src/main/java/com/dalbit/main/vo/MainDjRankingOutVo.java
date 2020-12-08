package com.dalbit.main.vo;

import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainDjRankingVo;
import com.dalbit.main.vo.procedure.P_MainRankingPageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MainDjRankingOutVo {

    private int rank;
    private String upDown;
    private String memNo;
    private String nickNm;
    private String memId;
    private String gender;
    private ImageVo profImg;
    private int level;
    private String grade;
    private int likes;
    private int listeners;
    private int broadcast;
    private int fan;
    private int gift;
    private int dj;
    private boolean isSpecial;
    private String holder;
    private String roomNo;
    private String listenRoomNo;
    private List<FanBadgeVo> liveBadgeList = new ArrayList<>();

    public MainDjRankingOutVo(){}
    public MainDjRankingOutVo(P_MainDjRankingVo target) {
        setRank(target.getRank());
        setUpDown(target.getUp_down());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setMemId(target.getMemId());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLevel(target.getLevel());
        setGrade(target.getGrade());
        setLikes(target.getGoodCount());
        setListeners(target.getListenerCount());
        setBroadcast(target.getBroadcastCount());
        setFan(target.getFanCount());
        setGift(target.getGiftCount());
        setDj(target.getDjCount());
        this.isSpecial = target.getSpecialdj_badge() == 1;
        this.holder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", this.level + "");
        setRoomNo(target.getRoomNo());
    }

    public MainDjRankingOutVo(P_MainRankingPageVo target) {
        setRank(target.getRank());
        setUpDown(target.getUp_down());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setMemId(target.getMemId());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLevel(target.getLevel());
        setGrade(target.getGrade());
        this.isSpecial = target.getSpecialdj_badge() == 1;
        this.holder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", this.level + "");
        setRoomNo(target.getRoomNo());
        if(!DalbitUtil.isEmpty(target.getLiveBadgeText()) && !DalbitUtil.isEmpty(target.getLiveBadgeIcon())){
            this.liveBadgeList.add(new FanBadgeVo(target.getLiveBadgeText(), target.getLiveBadgeIcon(), target.getLiveBadgeStartColor(), target.getLiveBadgeEndColor(), target.getLiveBadgeImage(), target.getLiveBadgeImageSmall()));
        }
        setListenRoomNo(target.getListenRoomNo());
    }
}
