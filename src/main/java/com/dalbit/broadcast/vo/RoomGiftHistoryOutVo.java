package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_RoomGiftHistoryVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RoomGiftHistoryOutVo {

    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private String itemNo;
    private String itemNm;
    private int gold;
    private Boolean isSecret;
    private long giftTs;
    private String giftDt;


    public RoomGiftHistoryOutVo(P_RoomGiftHistoryVo target) {
        this.memNo = target.getMem_no();
        this.nickNm = target.getNickName();
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.itemNo = target.getItem_no();
        this.itemNm = target.getItem_name();
        this.gold = target.getGold();
        this.isSecret = (target.getSecret() == 1) ? true : false;
        this.giftTs = DalbitUtil.getUTCTimeStamp(target.getGiftDate());
        this.giftDt = DalbitUtil.getUTCFormat(target.getGiftDate());
    }

}
