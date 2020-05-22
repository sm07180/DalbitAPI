package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_RoomInfoViewVo;
import com.dalbit.broadcast.vo.procedure.P_RoomListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

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
        this.byeolCnt = target.getCount_good();
        this.startDt = DalbitUtil.getUTCFormat(target.getStart_date());
        this.startTs = DalbitUtil.getUTCTimeStamp(target.getStart_date());
        this.bjMemNo = target.getBj_mem_no();
        this.bjNickNm = target.getBj_nickName();
        this.bjGender = target.getBj_memSex();
        this.bjAge = DalbitUtil.ageCalculation(target.getBj_birthYear());
        this.bjProfImg = new ImageVo(target.getBj_profileImage(), target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url"));
        this.gstMemNo = target.getGuest_mem_no();
        this.gstNickNm = target.getGuest_nickName();
        this.gstGender = target.getGuest_memSex();
        this.gstAge = DalbitUtil.ageCalculation(target.getGuest_birthYear());
        this.gstProfImg = new ImageVo(target.getGuest_profileImage(), target.getGuest_memSex(), DalbitUtil.getProperty("server.photo.url"));
        this.isRecomm = (target.getBadge_recomm() == 1) ? true : false;
        this.isPop = (target.getBadge_popular() == 1) ? true : false;
        this.isNew = (target.getBadge_newdj() == 1 ? true : false);
        this.startDt = DalbitUtil.getUTCFormat(target.getStart_date());
        this.startTs = DalbitUtil.getUTCTimeStamp(target.getStart_date());
        this.isSpecial = (target.getBadge_special() == 1 ? true : false);
    }

    public RoomOutVo(P_RoomInfoViewVo target) {
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
    }
}
