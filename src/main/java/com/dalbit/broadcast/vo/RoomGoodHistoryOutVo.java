package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_RoomGoodHistoryVo;
import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RoomGoodHistoryOutVo {

    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private int auth;
    private String joinDt;
    private Long joinTs;
    private Boolean isFan;
    private Boolean isNewListener;
    private Boolean isSpecial;
    private int liveFanRank;
    private List<FanBadgeVo> liveBadgeList = new ArrayList<>();
    private List<FanBadgeVo> commonBadgeList = new ArrayList<>();
    private int goodCnt;
    private Boolean isJoin;

    public RoomGoodHistoryOutVo(P_RoomGoodHistoryVo target){
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setAuth(target.getAuth());
        setJoinDt(DalbitUtil.getUTCFormat(target.getJoin_date()));
        setJoinTs(DalbitUtil.getUTCTimeStamp(target.getJoin_date()));
        setIsFan(target.getEnableFan() == 0 ? true : false);
        setIsNewListener(this.auth == 0 ? (target.getNewBadge() == 1 ? true : false) : false);
        setIsSpecial(target.getSpecialBadge() == 1 ? true : false);
        setLiveFanRank(target.getLiveFanRank());
        if(!DalbitUtil.isEmpty(target.getLiveBadgeText())){
            this.liveBadgeList.add(new FanBadgeVo(target.getLiveBadgeText(), target.getLiveBadgeIcon(), target.getLiveBadgeStartColor(), target.getLiveBadgeEndColor(), target.getLiveBadgeImage(), target.getLiveBadgeImageSmall()));
        }
        setGoodCnt(target.getGoodCnt());
        setIsJoin(target.getState() == 0 ? true : false);
    }

}
