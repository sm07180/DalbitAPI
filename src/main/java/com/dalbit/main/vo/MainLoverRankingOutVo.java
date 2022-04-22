package com.dalbit.main.vo;

import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import com.dalbit.main.vo.procedure.P_MainLoverRankingPageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MainLoverRankingOutVo {

    private int rank;
    private String upDown;
    private String memNo;
    private String nickNm;
    private String memId;
    private String gender;
    private ImageVo profImg;
    private int level;
    private String grade;
    private int gift;
    private int listen;
    private int fan;
    private boolean isSpecial;
    private int badgeSpecial;
    private String holder;
    private String roomNo;
    private String listenRoomNo;
    private List<FanBadgeVo> liveBadgeList = new ArrayList<>();

    public MainLoverRankingOutVo(){}
    public MainLoverRankingOutVo(P_MainFanRankingVo target) {
        setRank(target.getRank());
        setUpDown(target.getUp_down());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setMemId(target.getMemId());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLevel(target.getLevel());
        setGrade(target.getGrade());
        setGift(target.getGiftCount());
        setListen(target.getListenCount());
        setFan(target.getFanCount());
        this.isSpecial = target.getSpecialdj_badge() > 0;
        this.badgeSpecial = target.getSpecialdj_badge();
        this.holder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", this.level + "");
        setRoomNo(target.getRoomNo());
    }

    public MainLoverRankingOutVo(P_MainLoverRankingPageVo target, boolean isAdmin) {
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
        this.badgeSpecial = target.getSpecialdj_badge();
        this.holder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", this.level + "");
        setRoomNo(target.getRoomNo());
        if(!DalbitUtil.isEmpty(target.getLiveBadgeText()) && !DalbitUtil.isEmpty(target.getLiveBadgeIcon())){
            this.liveBadgeList.add(new FanBadgeVo(target.getLiveBadgeText(), target.getLiveBadgeIcon(), target.getLiveBadgeStartColor(), target.getLiveBadgeEndColor(), target.getLiveBadgeImage(), target.getLiveBadgeImageSmall()));
        }
//        setListenRoomNo(DalbitUtil.getListenRoomNo(target.getListenRoomNo(), target.getListenOpen(), isAdmin));
        setListenRoomNo(target.getListenRoomNo());
    }
}
