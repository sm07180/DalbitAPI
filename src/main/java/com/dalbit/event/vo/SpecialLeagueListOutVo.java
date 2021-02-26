package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.event.vo.procedure.P_SpecialLeagueVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpecialLeagueListOutVo {

    private int rank;
    private String upDown;
    private String memNo;
    private String nickName;
    private String gender;
    private ImageVo profImg;
    private int giftPoint;
    private int listenerPoint;
    private int goodPoint;
    private String fanPoint;
    private int totalPoint;

    public SpecialLeagueListOutVo(P_SpecialLeagueVo target){
        setRank(target.getRank());
        setUpDown(target.getUp_down());
        setMemNo(target.getMem_no());
        setNickName(target.getMem_nick());
        setGender(target.getMem_sex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        setGiftPoint(target.getGiftPoint());
        setListenerPoint(target.getListenerPoint());
        setGoodPoint(target.getGoodPoint());
        setFanPoint(target.getFanPoint());
        setTotalPoint(target.getTotalPoint());
    }

}
