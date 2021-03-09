package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_MypageReportListenVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MypageReportListenListOutVo {

    private String listenDt;
    private Long listenTs;
    private String startDt;
    private Long startTs;
    private String endDt;
    private Long endTs;
    private String bjNickNm;
    private int giftDalCnt;
    private int byeolCnt;
    private int listenTime;
    private Boolean isGuest;

    public MypageReportListenListOutVo(){}
    public MypageReportListenListOutVo(P_MypageReportListenVo target) {
        setListenDt(DalbitUtil.getUTCFormat(target.getListenDate()));
        setListenTs(DalbitUtil.getUTCTimeStamp(target.getListenDate()));
        setStartDt(DalbitUtil.getUTCFormat(target.getStart_date()));
        setStartTs(DalbitUtil.getUTCTimeStamp(target.getStart_date()));
        setEndDt(DalbitUtil.getUTCFormat(target.getEnd_date()));
        setEndTs(DalbitUtil.getUTCTimeStamp(target.getEnd_date()));
        setBjNickNm(target.getDj_nickname());
        setListenTime(target.getListentime());
        setGiftDalCnt(target.getGift_ruby());
        setByeolCnt(target.getGold());
        setIsGuest(target.getGuest() == 1);
    }
}
