package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_FanRankingVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FanRankingOutVo {

    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private Boolean isFan;
    private int giftDal;
    private int giftedByeol;

    public FanRankingOutVo(){}
    public FanRankingOutVo(P_FanRankingVo target){
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setIsFan(target.getEnableFan() == 0 ? true : false);
        setGiftDal(DalbitUtil.isEmpty(target.getGiftDal()) ? 0 : target.getGiftDal());
        setGiftedByeol(DalbitUtil.isEmpty(target.getGiftedByeol()) ? 0 : target.getGiftedByeol());
    }
}
