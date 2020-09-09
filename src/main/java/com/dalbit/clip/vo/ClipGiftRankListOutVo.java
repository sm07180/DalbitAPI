package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipGiftRankListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipGiftRankListOutVo {

    private String memNo;
    private String nickName;
    private String gender;
    private ImageVo profImg;
    private Boolean isFan;
    private int giftDal;

    public ClipGiftRankListOutVo(P_ClipGiftRankListVo target){
        setMemNo(target.getMem_no());
        setNickName(target.getNickName());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), getGender(), DalbitUtil.getProperty("server.photo.url")));
        setIsFan(target.getEnableFan() == 0 ? true : false);
        setGiftDal(target.getGiftDal());
    }
}
