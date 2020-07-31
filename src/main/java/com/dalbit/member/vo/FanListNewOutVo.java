package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_FanListNewVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class FanListNewOutVo {

    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private Boolean isFan;
    private String fanMemo;
    private int listenTime;
    private int giftedByeol;
    private String lastListenDt;
    private long lastListenTs;
    private String regDt;
    private long regTs;

    public FanListNewOutVo(){}
    public FanListNewOutVo(P_FanListNewVo target){
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setIsFan(target.getEnableFan() == 0 ? true : false);
        setFanMemo(target.getFanMemo());
        setListenTime(target.getListenTime());
        setGiftedByeol(target.getGiftedByeol());
        setLastListenDt(DalbitUtil.isEmpty(target.getLastlistenDate()) ? "" : DalbitUtil.getUTCFormat(target.getLastlistenDate()));
        setLastListenTs(DalbitUtil.isEmpty(target.getLastlistenDate()) ? 0 : DalbitUtil.getUTCTimeStamp(target.getLastlistenDate()));
        setRegDt(DalbitUtil.isEmpty(target.getRegDate()) ? "" : DalbitUtil.getUTCFormat(target.getRegDate()));
        setRegTs(DalbitUtil.isEmpty(target.getRegDate()) ? 0 : DalbitUtil.getUTCTimeStamp(target.getRegDate()));

    }
}
