package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipGiftListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipGiftListOutVo {

    private String memNo;
    private String nickName;
    private String gender;
    private ImageVo profImg;
    private String itemCode;
    private String itemName;
    private int giftByeol;
    private String giftDt;
    private long giftTs;
    private Boolean isFan;

    public ClipGiftListOutVo(P_ClipGiftListVo target){
        setMemNo(target.getMem_no());
        setNickName(target.getNickName());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), getGender(), DalbitUtil.getProperty("server.photo.url")));
        setItemCode(target.getItem_code());
        setItemName(target.getItem_name());
        setGiftByeol(target.getByeol());
        setGiftDt(DalbitUtil.getUTCFormat(target.getGiftDate()));
        setGiftTs(DalbitUtil.getUTCTimeStamp(target.getGiftDate()));
        setIsFan(target.getEnableFan() == 0 ? true : false);
    }
}
