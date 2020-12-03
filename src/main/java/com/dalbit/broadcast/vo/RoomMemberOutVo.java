package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_RoomMemberListVo;
import com.dalbit.common.vo.BadgeVo;
import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public class RoomMemberOutVo{

    private String memNo;
    private String nickNm;
    private String memId;
    private String gender;
    private int age;
    private ImageVo profImg;
    private int auth;
    private String ctrlRole;
    private String joinDt;
    private long joinTs;
    private boolean isFan;
    private int byeolCnt;
    private boolean isNewListener;
    private boolean isSpecial;
    private int liveFanRank;
    private List<BadgeVo> liveBadgeList = new ArrayList<>();
    private List<BadgeVo> commonBadgeList = new ArrayList<>();
    private int goodCnt;
    private Boolean isGuest;
    private int managerType;

    public RoomMemberOutVo(P_RoomMemberListVo target){
        this.memNo = target.getMem_no();
        this.nickNm = target.getNickName();
        this.memId = target.getUserID();
        this.gender = target.getMemSex();
        this.age = DalbitUtil.ageCalculation(target.getBirthYear());
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.auth = target.getAuth();
        this.ctrlRole = target.getControlRole();
        this.joinDt = DalbitUtil.getUTCFormat(target.getJoin_date());
        this.joinTs = DalbitUtil.getUTCTimeStamp(target.getJoin_date());
        this.isFan = target.getEnableFan() == 0 ? true : false;
        this.byeolCnt = target.getGift_gold();
        this.isNewListener = this.auth == 0 ? (target.getNewBadge() == 1 ? true : false) : false;
        this.isSpecial = target.getSpecialBadge() == 1 ? true : false;
        this.liveFanRank = target.getLiveFanRank();
        /*if(!DalbitUtil.isEmpty(target.getLiveBadgeText())){
            this.liveBadgeList.add(new FanBadgeVo(target.getLiveBadgeText(), target.getLiveBadgeIcon(), target.getLiveBadgeStartColor(), target.getLiveBadgeEndColor(), target.getLiveBadgeImage(), target.getLiveBadgeImageSmall()));
        }*/
        this.goodCnt = target.getGoodCnt();
        this.isGuest = target.getIsGuest() == 1 ? true : false;
        this.managerType = target.getManagerType();
    }
}
