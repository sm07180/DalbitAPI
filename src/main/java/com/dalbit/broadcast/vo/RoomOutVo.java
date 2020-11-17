package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_RoomInfoViewVo;
import com.dalbit.broadcast.vo.procedure.P_RoomListVo;
import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.PagingVo;
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
    private String notice;
    private int state;
    private String link;
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
    private Boolean isAttendCheck;
    private String isAttendUrl;
    private int os;
    private int isWowza;
    private int totalCnt = 0;
    private Boolean isFreeze;
    private int liveDjRank;
    private List<FanBadgeVo> liveBadgeList = new ArrayList<>();
    private int freezeMsg;
    private Boolean isExtend;
    private int imageType;

    public RoomOutVo(P_RoomListVo target) {

        this.roomNo = target.getRoomNo();
        this.roomType = target.getSubject_type();
        this.title = target.getTitle();
        this.bgImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        this.welcomMsg = target.getMsg_welcom();
        this.entryType = target.getType_entry();
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
            this.bjProfImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        }else{
            this.bjProfImg = new ImageVo(target.getBj_profileImage(), target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url"));
        }
        this.gstMemNo = target.getGuest_mem_no();
        this.gstNickNm = target.getGuest_nickName();
        this.gstGender = target.getGuest_memSex();
        this.gstAge = DalbitUtil.ageCalculation(target.getGuest_birthYear());
        this.gstProfImg = new ImageVo(target.getGuest_profileImage(), target.getGuest_memSex(), DalbitUtil.getProperty("server.photo.url"));
        this.isRecomm = (target.getBadge_recomm() == 1) ? true : false;
        this.isPop = (target.getBadge_popular() == 1) ? true : false;
        this.isNew = (target.getBadge_newdj() == 1 ? true : false);
        this.isSpecial = (target.getBadge_special() == 1 ? true : false);
        this.boostCnt = target.getCount_boost();
        this.rank = target.getRank();
        this.os = target.getOs_type();
        this.isWowza = target.getIs_wowza();
        this.totalCnt = target.getTotalCnt();
        this.liveDjRank = target.getLiveDjRank() > 100 ? 0 : target.getLiveDjRank();
        if(!DalbitUtil.isEmpty(target.getLiveBadgeText())){
            this.liveBadgeList.add(new FanBadgeVo(target.getLiveBadgeText(), target.getLiveBadgeIcon(), target.getLiveBadgeStartColor(), target.getLiveBadgeEndColor(), target.getLiveBadgeImage(), target.getLiveBadgeImageSmall()));
        }
    }

    public RoomOutVo(P_RoomInfoViewVo target, HashMap attendanceCheckMap) {
        this.roomNo = target.getRoomNo();
        this.roomType = target.getSubject_type();
        this.title = target.getTitle();
        this.bgImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        this.welcomMsg = target.getMsg_welcom();
        this.entryType = target.getType_entry();
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
        this.isRecomm = (target.getBadge_recomm() == 1) ? true : false;
        this.isPop = (target.getBadge_popular() == 1) ? true : false;
        this.isNew = (target.getBadge_newdj() == 1 ? true : false);
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
        this.isSpecial = (target.getBadge_special() == 1 ? true : false);
        //TODO 출석체크이벤트 종료 시 구분 처리 필요
        this.isAttendCheck = (Boolean) attendanceCheckMap.get("isCheck");
        this.isAttendUrl = DalbitUtil.getProperty("server.mobile.url") + "/event/attend_event?webview=new";

        this.isFreeze = (target.getFreezeMsg() == 0) ? false : true;
        this.liveDjRank = target.getLiveDjRank() > 100 ? 0 : target.getLiveDjRank();
        if(!DalbitUtil.isEmpty(target.getLiveBadgeText())){
            this.liveBadgeList.add(new FanBadgeVo(target.getLiveBadgeText(), target.getLiveBadgeIcon(), target.getLiveBadgeStartColor(), target.getLiveBadgeEndColor(), target.getLiveBadgeImage(), target.getLiveBadgeImageSmall()));
        }

        this.os = target.getOs();
        this.freezeMsg = target.getFreezeMsg();
        this.isExtend = target.getExtendCnt() > 0 ? true : false;
        this.imageType = target.getImageType();
    }
}
