package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_RoomShareLinkVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;

public class RoomShareLinkOutVo {

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
    private String bjNickNm;
    private String bjGender;
    private int bjAge;
    private ImageVo bjProfImg;
    private String gstMemNo;
    private String gstNickNm;
    private String gstGender;
    private int gstAge;
    private ImageVo gstProfImg;

    public RoomShareLinkOutVo(P_RoomShareLinkVo target){
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
        this.bjNickNm = target.getBj_nickName();
        this.bjGender = target.getBj_memSex();
        this.bjAge = DalbitUtil.ageCalculation(target.getBj_birthYear());
        this.bjProfImg = new ImageVo(target.getBj_profileImage(), target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url"));
        this.gstMemNo = target.getGuest_mem_no();
        this.gstNickNm = target.getGuest_nickName();
        this.gstGender = target.getGuest_memSex();
        this.gstAge = target.getGuest_birthYear();
        this.gstProfImg = new ImageVo(target.getGuest_profileImage(), target.getGuest_memSex(), DalbitUtil.getProperty("server.photo.url"));
    }
}
