package com.dalbit.main.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import com.dalbit.main.vo.procedure.P_MainMyDjVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class MainMyDjOutVo {

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
    private int byeolCnt;
    private long startTs;
    private String startDt;
    private Boolean isRecomm;
    private Boolean isPop;
    private Boolean isNew;
    private int airtime;
    private String bjMemNo;
    private String bjNickNm;
    private String bjGender;
    private int bjAge;
    private ImageVo bjProfImg;

    public MainMyDjOutVo(){}
    public MainMyDjOutVo(P_MainMyDjVo target) {
        setRoomNo(target.getRoomNo());
        setRoomType(target.getSubject_type());
        setTitle(target.getTitle());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setWelcomMsg(target.getMsg_welcom());
        setEntryType(target.getType_entry());
        setNotice(target.getNotice());
        setState(target.getState());
        setLink(target.getCode_link());
        setEntryCnt(target.getCount_entry());
        setLikeCnt(target.getCount_good());
        setByeolCnt(target.getCount_gold());
        setStartDt(DalbitUtil.getUTCFormat(target.getStart_date()));
        setStartTs(DalbitUtil.getUTCTimeStamp(target.getStart_date()));
        setIsRecomm(target.getBadge_recomm() == 1 ? true : false);
        setIsPop(target.getBadge_popular() == 1 ? true : false);
        setIsNew(target.getBadge_newdj() == 1 ? true : false);
        setAirtime(target.getAirtime());
        setBjMemNo(target.getBj_mem_no());
        setBjNickNm(target.getBj_nickName());
        setBjGender(target.getBj_memSex());
        setBjAge(DalbitUtil.ageCalculation(target.getBj_birthYear()));
        setBjProfImg(new ImageVo(target.getBj_profileImage(), target.getBj_memSex(), DalbitUtil.getProperty("server.photo.url")));
    }
}
