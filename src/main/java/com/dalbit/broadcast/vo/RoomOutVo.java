package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_RoomInfoViewVo;
import com.dalbit.broadcast.vo.procedure.P_RoomListVo;
import com.dalbit.common.vo.*;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter @Setter @ToString
public class RoomOutVo {

    private String roomNo;
    private String roomType;
    private String title;
    private ImageVo bgImg;
    private String welcomMsg;
    private int entryType;
    private String mediaType;
    private String notice;
    private int state;
    private String link;
    private int newFanCnt;
    private int entryCnt;
    private int likeCnt;
    private int boostCnt;
    private int rank;
    private int giftCnt;
    private String startDt;
    private long startTs;
    private String bjMemNo;
    private String bjMemId;
    private String bjNickNm;
    private String bjGender;
    private int bjAge;
    private ImageVo bjProfImg;
    private int bjLevel;
    private String gstMemNo;
    private String gstMemId;
    private String gstNickNm;
    private String gstGender;
    private int gstAge;
    private ImageVo gstProfImg;
    private int gstLevel;
    private PagingVo paging;
    private int level;
    private int exp;
    private int expNext;
    private String grade;
    private int dalCnt;
    private int byeolCnt;
    private Boolean isRecomm;
    private Boolean isPop;
    private Boolean isNew;
    private Boolean isSpecial;
    private int badgeSpecial;
    private Boolean isAttendCheck;
    private String isAttendUrl;
    private int os;
    private int isWowza;
    private int totalCnt = 0;
    private Boolean isFreeze;
    private int liveDjRank;
    private List<BadgeVo> liveBadgeList = new ArrayList<>();
    private List<BadgeVo> commonBadgeList = new ArrayList<>();
    private int freezeMsg;
    private Boolean isExtend;
    private int imageType;
    private Boolean isGoodMem;
    private boolean isShining;
    private MoonCheckInfoVo moonCheck = new MoonCheckInfoVo();
    private int step;
    private int oldStep;
    private int completeMoon;
    private int fullmoon_yn;
    private List<Integer> goodMem = new ArrayList();
    private boolean isMic;
    private boolean isCall;
    private boolean isServer;
    private boolean isVideo;
    private boolean isMinigame;
    private boolean isConDj;
    private boolean isVote;
    private int badge_partner;

    public RoomOutVo(P_RoomListVo target) {
        setRoomOutVo(target, null);
    }

    public RoomOutVo(P_RoomListVo target, DeviceVo deviceVo) {
        setRoomOutVo(target, deviceVo);
    }

    public void setRoomOutVo(P_RoomListVo target, DeviceVo deviceVo) {

        this.roomNo = target.getRoomNo();
        this.roomType = target.getSubject_type();
        this.title = target.getTitle();
        this.bgImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        this.welcomMsg = target.getMsg_welcom();
        this.entryType = target.getType_entry();
        this.mediaType = target.getType_media();
        this.notice = target.getNotice();
        this.state = target.getState();
        this.link = target.getCode_link();
        this.entryCnt = target.getCount_entry();
        this.likeCnt = target.getCount_good();
        this.giftCnt = target.getCount_gold();
        this.startDt = DalbitUtil.getUTCFormat(target.getStart_date());
        this.startTs = DalbitUtil.getUTCTimeStamp(target.getStart_date());
        this.bjMemNo = target.getBj_mem_no();
        this.bjNickNm = target.getBj_nickName();
        this.bjGender = target.getBj_memSex();
        this.bjAge = DalbitUtil.ageCalculation(target.getBj_birthYear());
        if(target.getType_image() == 2){    //스페셜DJ일 경우 실시간live 이미지 노출선택(1:프로필, 2:배경)
            if(deviceVo != null && target.getImage_background().toString().toLowerCase().endsWith(".gif")){
                if(deviceVo.getOs() == 2 && DalbitUtil.versionCompare("14.0", deviceVo.getSdkVersion())){
                    this.bjProfImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
                }else{ //webp사용
                    this.bjProfImg = new ImageVo(target.getImage_background().toString() + ".webp", DalbitUtil.getProperty("server.photo.url"));
                }
            }else{
                this.bjProfImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
            }
        }else{
            this.bjProfImg = new ImageVo(target.getBj_profileImage(), target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url"));
        }
        this.gstMemNo = target.getGuest_mem_no();
        this.gstNickNm = target.getGuest_nickName();
        this.gstGender = target.getGuest_memSex();
        this.gstAge = DalbitUtil.ageCalculation(target.getGuest_birthYear());
        this.gstProfImg = new ImageVo(target.getGuest_profileImage(), target.getGuest_memSex(), DalbitUtil.getProperty("server.photo.url"));
        this.isRecomm = target.getBadge_recomm() == 1;
        this.isPop = target.getBadge_popular() == 1;
        this.isNew = target.getBadge_newdj() == 1;
        this.isSpecial = target.getBadge_special() > 0;
        this.isConDj = target.getIsConDj() > 0;
        this.badgeSpecial = target.getBadge_special();
        this.boostCnt = target.getCount_boost();
        this.rank = target.getRank();
        this.os = target.getOs_type();
        this.isWowza = target.getIs_wowza();
        this.totalCnt = target.getTotalCnt();
        this.liveDjRank = target.getLiveDjRank() > 100 ? 0 : target.getLiveDjRank();
        if(!DalbitUtil.isEmpty(target.getLiveBadgeText()) && !DalbitUtil.isEmpty(target.getLiveBadgeIcon())){
            BadgeVo badgeVo = new BadgeVo();
            badgeVo.setIcon(target.getLiveBadgeIcon());
            badgeVo.setText(target.getLiveBadgeText());
            badgeVo.setStartColor(target.getLiveBadgeStartColor());
            badgeVo.setEndColor(target.getLiveBadgeEndColor());
            this.liveBadgeList.add(badgeVo);
        }
        if(!DalbitUtil.isEmpty(target.getFanBadgeText())){
            BadgeVo badgeVo = new BadgeVo();
            badgeVo.setIcon(target.getFanBadgeIcon());
            badgeVo.setText(target.getFanBadgeText());
            badgeVo.setStartColor(target.getFanBadgeStartColor());
            badgeVo.setEndColor(target.getFanBadgeEndColor());
            this.liveBadgeList.add(badgeVo);
        }
        if(target.getGoodMem() == 1){
            this.goodMem.add(1);
        }
        if(target.getGoodMem2() == 1){
            this.goodMem.add(2);
        }
        if(target.getGoodMem3() == 1){
            this.goodMem.add(3);
        }
        this.isGoodMem = (target.getGoodMem() + target.getGoodMem2() + target.getGoodMem3()) > 0;
        this.isShining = target.isShining();
        this.newFanCnt = target.getCount_fan();
    }

    public RoomOutVo(P_RoomInfoViewVo target, HashMap attendanceCheckMap, HashMap moonCheckMap, boolean isMiniGame) {
        this.roomNo = target.getRoomNo();
        this.roomType = target.getSubject_type();
        this.title = target.getTitle();
        this.bgImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        this.welcomMsg = target.getMsg_welcom();
        this.entryType = target.getType_entry();
        this.mediaType = target.getType_media();
        this.notice = target.getNotice();
        this.state = target.getState();
        this.link = target.getCode_link();
        this.entryCnt = target.getCount_entry();
        this.likeCnt = target.getCount_good();
        this.startDt = DalbitUtil.getUTCFormat(target.getStart_date());
        this.startTs = DalbitUtil.getUTCTimeStamp(target.getStart_date());
        this.bjMemNo = target.getBj_mem_no();
        this.bjMemId = target.getBj_userId();
        this.bjNickNm = target.getBj_nickName();
        this.bjGender = target.getBj_memSex();
        this.bjAge = DalbitUtil.ageCalculation(target.getBj_birthYear());
        this.bjProfImg = new ImageVo(target.getBj_profileImage(), target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url"));
        this.gstMemNo = target.getGuest_mem_no();
        this.gstMemId = target.getGuest_userId();
        this.gstNickNm = target.getGuest_nickName();
        this.gstGender = target.getGuest_memSex();
        this.gstAge = DalbitUtil.ageCalculation(target.getGuest_birthYear());
        this.gstProfImg = new ImageVo(target.getGuest_profileImage(), target.getGuest_memSex(), DalbitUtil.getProperty("server.photo.url"));
        this.isRecomm = target.getBadge_recomm() == 1;
        this.isPop = target.getBadge_popular() == 1;
        this.isNew = target.getBadge_newdj() == 1;
        this.startDt = DalbitUtil.getUTCFormat(target.getStart_date());
        this.startTs = DalbitUtil.getUTCTimeStamp(target.getStart_date());

        HashMap resultMap = new Gson().fromJson(target.getExt(), HashMap.class);
        this.level = DalbitUtil.getIntMap(resultMap, "level");
        this.grade = DalbitUtil.getStringMap(resultMap, "grade");
        this.exp = DalbitUtil.getIntMap(resultMap, "exp");
        this.expNext = DalbitUtil.getIntMap(resultMap, "expNext");
        this.dalCnt = DalbitUtil.getIntMap(resultMap, "ruby");
        this.byeolCnt = DalbitUtil.getIntMap(resultMap, "gold");

        this.bjLevel = target.getBj_level();
        this.gstLevel = target.getGuest_level();
        this.isSpecial = target.getBadge_special() > 0;
        this.isConDj = target.getIsConDj() > 0;
        this.badgeSpecial = target.getBadge_special();
        this.badge_partner = target.getBadge_partner();
        //TODO 출석체크이벤트 종료 시 구분 처리 필요
        this.isAttendCheck = (Boolean) attendanceCheckMap.get("isCheck");
        this.isAttendUrl = DalbitUtil.getProperty("server.mobile.url") + "/event/attend_event?webview=new";

        this.isFreeze = target.getFreezeMsg() > 0;
        this.liveDjRank = target.getLiveDjRank() > 100 ? 0 : target.getLiveDjRank();
        /*if(!DalbitUtil.isEmpty(target.getLiveBadgeText())){
            this.liveBadgeList.add(new FanBadgeVo(target.getLiveBadgeText(), target.getLiveBadgeIcon(), target.getLiveBadgeStartColor(), target.getLiveBadgeEndColor(), target.getLiveBadgeImage(), target.getLiveBadgeImageSmall()));
        }*/

        this.os = target.getOs();
        this.freezeMsg = target.getFreezeMsg();
        this.isExtend = target.getExtendCnt() > 0;
        this.imageType = target.getImageType();

        moonCheck.setMoonStep(DalbitUtil.getIntMap(moonCheckMap, "moonStep"));
        moonCheck.setMoonStepFileNm(DalbitUtil.getStringMap(moonCheckMap, "moonStepFileNm"));
        moonCheck.setMoonStepAniFileNm(DalbitUtil.getStringMap(moonCheckMap, "moonStepAniFileNm"));
        moonCheck.setDlgTitle(DalbitUtil.getStringMap(moonCheckMap, "dlgTitle"));
        moonCheck.setDlgText(DalbitUtil.getStringMap(moonCheckMap, "dlgText"));
        moonCheck.setAniDuration(DalbitUtil.getIntMap(moonCheckMap, "aniDuration"));

        this.step = DalbitUtil.getIntMap(moonCheckMap, "moonStep");
        this.oldStep = DalbitUtil.getIntMap(moonCheckMap, "oldStep");
        this.completeMoon = target.getCompleteMoon();
        this.fullmoon_yn = target.getFullmoon_yn();
        this.isMic = target.getMic_state() == 1;
        this.isCall = target.getCall_state() == 1;
        this.isServer = target.getServer_state() == 1;
        this.isVideo = target.getVideo_state() == 1;
        this.isMinigame = isMiniGame;
        this.newFanCnt = target.getCount_fan();
    }

    public void setIsVote(boolean isVote){
        this.isVote = isVote;
    }
}
