package com.dalbit.main.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainRankingPageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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
    private boolean isSpecial;
    private int djPoint;
    private int listenerPoint;
    private int goodPoint;
    private int broadcastPoint;
    private int giftPoint;
    private int fanPoint;
    private int listenPoint;
    private String holder;
    private String roomNo;

    public MainRankingPageOutVo(){}
    public MainRankingPageOutVo(P_MainRankingPageVo target) {
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
    }
}
