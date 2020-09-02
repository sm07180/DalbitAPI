package com.dalbit.broadcast.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter @Setter
public class RoomInfoVo {
    /* 방정보 */
    private String roomNo;
    private String title;
    private ImageVo bgImg;
    private int state;
    private String startDt;
    private long startTs;
    private boolean hasNotice;
    private boolean hasStory;
    private boolean useBoost;
    private long likes;
    private long rank;
    private String roomType;
    private String welcomMsg;
    private int entryType;
    /* DJ정보 */
    private String bjMemNo;
    private String bjNickNm;
    private String bjHolder;
    private ImageVo bjProfImg;
    private List fanBadgeList;
    private List fanRank;
    private boolean isRecomm;
    private boolean isPop;
    private boolean isNew;
    private boolean isSpecial;
    /* 요청자 정보 */
    private int auth;
    private String ctrlRole;
    private boolean isFan;
    private boolean isLike;
    private long remainTime;
    /* 황제팬 정보 */
    private String kingMemNo;
    private String kingNickNm;
    private ImageVo kingProfImg;
    /* WOWZA 정보 */
    private String rtmpOrigin;
    private String rtmpEdge;
    private String webRtcUrl;
    private String webRtcAppName;
    private String webRtcStreamName;
    /* 게스트 : streamName => wowza.prefix(DEV/REAL) + room_no + _ + mem_no */
    private List guests = new ArrayList();

    public RoomInfoVo(RoomOutVo target, RoomMemberInfoVo memberInfoVo, String wowza_prefix){
        this.roomNo = target.getRoomNo();
        this.title = target.getTitle();
        this.bgImg = target.getBgImg();
        this.state = target.getState();
        this.bjMemNo = target.getBjMemNo();
        this.bjNickNm = target.getBjNickNm();
        this.bjProfImg = target.getBjProfImg();
        this.bjHolder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", target.getBjLevel() + "");
        this.isRecomm = target.getIsRecomm();
        this.isPop = target.getIsPop();
        this.isNew = target.getIsNew();
        this.isSpecial = target.getIsSpecial();
        this.startDt = target.getStartDt();
        this.startTs = target.getStartTs();
        this.hasNotice = this.auth == 3 ? false : !DalbitUtil.isEmpty(target.getNotice());
        this.roomType = target.getRoomType();
        this.welcomMsg = target.getWelcomMsg();
        this.entryType = target.getEntryType();

        this.auth = memberInfoVo.getAuth();
        this.hasStory = memberInfoVo.isHashStory();
        this.remainTime = memberInfoVo.getRemainTime();
        this.useBoost = memberInfoVo.isUseBoost();
        this.ctrlRole = memberInfoVo.getCtrlRole();
        this.isFan = memberInfoVo.isFan();
        this.isLike = memberInfoVo.isLike();
        this.fanBadgeList = memberInfoVo.getFanBadgeList();
        this.fanRank = memberInfoVo.getFanRank();
        this.kingMemNo = memberInfoVo.getKingMemNo();
        this.kingNickNm = memberInfoVo.getKingNickNm();
        this.kingProfImg = memberInfoVo.getKingProfImg();
        this.rank = memberInfoVo.getRank();
        this.likes = memberInfoVo.getLikes();
        if(DalbitUtil.isEmpty(this.kingProfImg)){
            this.kingProfImg = new ImageVo();
        }
        if(DalbitUtil.isEmpty(this.fanBadgeList)){
            this.fanBadgeList = new ArrayList();
        }
        if(DalbitUtil.isEmpty(this.fanRank)){
            this.fanRank = new ArrayList();
        }

        if(this.rank < 1){
            this.rank = target.getRank();
        }

        this.webRtcStreamName = wowza_prefix + this.roomNo;
        this.rtmpOrigin = DalbitUtil.getProperty("wowza.rtmp.origin") + "/" + webRtcStreamName;// + "_aac";
        this.rtmpEdge = DalbitUtil.getProperty("wowza.rtmp.edge") + "/" + webRtcStreamName + "_aac";
        this.webRtcUrl = DalbitUtil.getProperty("wowza.wss.url") ;
        this.webRtcAppName = this.auth == 3 ? "origin" : "edge";
        if(this.auth != 3){
            this.webRtcStreamName = wowza_prefix + this.roomNo + "_opus";
        }
    }
}
