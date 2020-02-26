package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_MypageReportBroadVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MypageReportBroadListOutVo {

    private String broadDt;
    private Long broadTs;
    private String startDt;
    private Long startTs;
    private String endDt;
    private Long endTs;
    private int airTime;
    private int byeolCnt;
    private int likes;
    private int listenerCnt;
    private int rank;

    public MypageReportBroadListOutVo(){}
    public MypageReportBroadListOutVo(P_MypageReportBroadVo target) {
        setBroadDt(DalbitUtil.getUTCFormat(target.getBroadcastDate()));
        setBroadTs(DalbitUtil.getUTCTimeStamp(target.getBroadcastDate()));
        setStartDt(DalbitUtil.getUTCFormat(target.getStart_date()));
        setStartTs(DalbitUtil.getUTCTimeStamp(target.getStart_date()));
        setEndDt(DalbitUtil.getUTCFormat(target.getEnd_date()));
        setEndTs(DalbitUtil.getUTCTimeStamp(target.getEnd_date()));
        setAirTime(target.getAirTime());
        setByeolCnt(target.getGold());
        setLikes(target.getGood());
        setListenerCnt(target.getListener());
        setRank(target.getRank());

    }
}
