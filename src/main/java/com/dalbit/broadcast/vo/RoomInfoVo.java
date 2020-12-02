package com.dalbit.broadcast.vo;

import com.dalbit.common.vo.*;
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
    private boolean djListenerIn;
    private boolean djListenerOut;
    private boolean listenerIn;
    private boolean listenerOut;
    private boolean liveBadgeView = true;
    private boolean isFreeze;
    private boolean isExtend;
    /* DJ정보 */
    private String bjMemNo;
    private String bjNickNm;
    private String bjHolder;
    private ImageVo bjProfImg;
    private List fanBadgeList;
    private List liveBadgeList;
    private List fanRank;
    private boolean isRecomm;
    private boolean isPop;
    private boolean isNew;
    private boolean isSpecial;
    private boolean isAttendCheck;  //출석체크 여부
    private String isAttendUrl;  //출석체크 이벤트 URL
    private int liveDjRank;
    private int os;             //bj os정보
    private int imageType;
    /* 요청자 정보 */
    private int auth;
    private String ctrlRole;
    private boolean isFan;
    private boolean isLike;
    private long remainTime;
    private Boolean isGuest;
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
    private int bufferingLow = 3000;
    private int bufferingHigh = 2500;
    private int guestBuffering = 500;
    private int chatEndInterval = 1000;
    /* 게스트 : streamName => wowza.prefix(DEV/REAL) + room_no + _ + mem_no */
    private List guests = new ArrayList();
    private boolean useGuest = true;

    /* 뱃지 정책 변경에 따른 데이터 20.11.25 이재은 */
    private List<BadgeVo> commonBadgeList = new ArrayList<>();
    private BadgeFrameVo badgeFrame = new BadgeFrameVo();

    public RoomInfoVo(RoomOutVo target, RoomMemberInfoVo memberInfoVo, String wowza_prefix, HashMap settingMap, HashMap attendanceCheckMap, DeviceVo deviceVo){
        this.roomNo = target.getRoomNo();
        this.title = target.getTitle();
        this.bgImg = target.getBgImg();
        this.state = target.getState();
        this.bjMemNo = target.getBjMemNo();
        this.bjNickNm = target.getBjNickNm();
        this.bjProfImg = target.getBjProfImg();
        if("real".equals(DalbitUtil.getActiveProfile()) && (
            (deviceVo.getOs() == 1 && Integer.parseInt(deviceVo.getAppBuild()) < 41)
            ||
            (deviceVo.getOs() == 2 && DalbitUtil.versionCompare("1.3.4", deviceVo.getAppVersion()))
        )){
            this.bjHolder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", target.getBjLevel() + "");
        }else{
            this.bjHolder = StringUtils.replace(DalbitUtil.getProperty("level.broad.frame"),"[level]", target.getBjLevel() + "");
        }
        //this.bjHolder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", target.getBjLevel() + "");
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
        this.djListenerIn = (boolean) settingMap.get("djListenerIn");
        this.djListenerOut = (boolean) settingMap.get("djListenerOut");
        this.listenerIn = (boolean) settingMap.get("listenerIn");
        this.listenerOut = (boolean) settingMap.get("listenerOut");
        //this.liveBadgeView = (boolean) settingMap.get("liveBadgeView");

        //TODO 출석체크이벤트 종료 시 구분 처리 필요 추후...
        if(memberInfoVo.getAuth() == 3){
            this.isAttendCheck = true;   //방장일 경우 비노출
        }else {
            this.isAttendCheck = (boolean) attendanceCheckMap.get("isCheck");
        }

        this.isAttendUrl = DalbitUtil.getProperty("server.mobile.url")+"/event/attend_event?webview=new";
        this.isFreeze = target.getIsFreeze();

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

        this.liveBadgeList = memberInfoVo.getLiveBadgeList();
        this.liveDjRank = target.getLiveDjRank() > 100 ? 0 : target.getLiveDjRank();
        this.os = target.getOs();
        this.isExtend = target.getIsExtend();
        this.imageType = target.getImageType();
    }

    public void changeBackgroundImg(DeviceVo deviceVo){
        if(("iPhone 6".equals(deviceVo.getDeviceModel()) || "iPhone 6 Plus".equals(deviceVo.getDeviceModel())) && this.bgImg.getUrl().endsWith(".gif")){
            this.bgImg.setUrl(this.bgImg.getUrl() + "?750x1334");
        }
    }
}
