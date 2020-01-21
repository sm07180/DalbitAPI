package com.dalbit.broadcast.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;

@Getter @Setter @ToString
public class RoomOutVo {

    private String roomNo;
    private int type;
    private String title;
    private ImageVo bgImg;
    private String welcomMsg;
    private int isEntry;
    private int isAge;
    private String notice;
    private int state;
    private String link;
    private int entryCnt;
    private int goodCnt;
    private String startDt;
    private long startTs;
    private String bjMemNo;
    private String bjNickNM;
    private String bjGender;
    private int bjAge;
    private ImageVo bjProfImg;
    private String gstMemNo;
    private String gstNickNm;
    private String gstGender;
    private int gstAge;
    private ImageVo gstProfImg;

    public RoomOutVo(P_RoomListVo target) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        this.roomNo = target.getRoomNo();
        this.type = target.getSubject_type();
        this.title = target.getTitle();
        this.bgImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        this.welcomMsg = target.getMsg_welcom();
        this.isEntry = target.getRestrict_entry();
        this.isAge = target.getRestrict_age();
        this.notice = target.getNotice();
        this.state = target.getState();
        this.link = target.getCode_link();
        this.entryCnt = target.getCount_entry();
        this.goodCnt = target.getCount_good();
        this.startDt = format.format(target.getStart_date());
        this.startTs = target.getStart_date().getTime() / 1000;
        this.bjMemNo = target.getBj_mem_no();
        this.bjNickNM = target.getBj_nickName();
        this.bjGender = target.getBj_memSex();
        this.bjAge = DalbitUtil.ageCalculation(target.getBj_birthYear());
        this.bjProfImg = new ImageVo(target.getBj_profileImage(), target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url"));
        this.gstMemNo = target.getGuest_mem_no();
        this.gstNickNm = target.getGuest_nickName();
        this.gstGender = target.getGuest_memSex();
        this.gstAge = DalbitUtil.ageCalculation(target.getGuest_birthYear());
        this.gstProfImg = new ImageVo(target.getGuest_profileImage(), target.getGuest_memSex(), DalbitUtil.getProperty("server.photo.url"));
    }

}