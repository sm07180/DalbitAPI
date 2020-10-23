package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.event.vo.procedure.P_RouletteApplyListVo;
import com.dalbit.event.vo.procedure.P_RouletteWinListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouletteWinListOutVo {

    private String memNo;
    private int itemNo;
    private Boolean isNew;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private String winDt;
    private long winTs;

    public RouletteWinListOutVo(P_RouletteWinListVo target){
        setMemNo(target.getMem_no());
        setItemNo(target.getItem_no());
        setIsNew(target.getNew_yn() == 1 ? true : false);
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setWinDt(DalbitUtil.getUTCFormat(target.getWin_date()));
        setWinTs(DalbitUtil.getUTCTimeStamp(target.getWin_date()));
    }
}
