package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_StarListNewVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StarListNewOutVo {

    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private Boolean isFan;
    private String starMemo;
    private int listenTime;
    private int giftedByeol;
    private String lastListenDt;
    private long lastListenTs;
    private String regDt;
    private long regTs;
    private String gender;

    public StarListNewOutVo(){}
    public StarListNewOutVo(P_StarListNewVo target){
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setIsFan(target.getEnableFan() == 0 ? true : false);
        setStarMemo(target.getStarMemo());
        setListenTime(target.getListenTime());
        setGiftedByeol(target.getGiftDal());
        setLastListenDt(DalbitUtil.isEmpty(target.getLastlistenDate()) ? "" : DalbitUtil.getUTCFormat(target.getLastlistenDate()));
        setLastListenTs(DalbitUtil.isEmpty(target.getLastlistenDate()) ? 0 : DalbitUtil.getUTCTimeStamp(target.getLastlistenDate()));
        setRegDt(DalbitUtil.isEmpty(target.getRegDate()) ? "" : DalbitUtil.getUTCFormat(target.getRegDate()));
        setRegTs(DalbitUtil.isEmpty(target.getRegDate()) ? 0 : DalbitUtil.getUTCTimeStamp(target.getRegDate()));
        setGender(target.getMemSex());
    }
}
