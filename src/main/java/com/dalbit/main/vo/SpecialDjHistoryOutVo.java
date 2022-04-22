package com.dalbit.main.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_SpecialHistoryVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter @Setter
public class SpecialDjHistoryOutVo {
    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private int level;
    private String holder;
    private String holderBg;
    private String profileBg;
    private int specialCnt;
    private int goodCnt;
    private int listenerCnt;
    private int broadMin;
    private String roomNo;
    private String[] levelColor;
    private boolean isNew;
    private String fanYn;

    public SpecialDjHistoryOutVo(){}
    public SpecialDjHistoryOutVo(P_SpecialHistoryVo pSpecialHistoryVo){
        this.memNo = pSpecialHistoryVo.getMem_no();
        this.nickNm = pSpecialHistoryVo.getMem_nick();
        this.gender = pSpecialHistoryVo.getMem_sex();
        this.profImg = new ImageVo(pSpecialHistoryVo.getImage_profile(), pSpecialHistoryVo.getMem_sex(), DalbitUtil.getProperty("server.photo.url"));
        this.level = pSpecialHistoryVo.getLevel();
        this.holder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", this.level + "");
        int l = (this.level - 1) / 10;
        this.holderBg = DalbitUtil.getLevelFrameBg(this.level);
        this.levelColor = DalbitUtil.getProperty("level.color." + l).split(",");
        this.specialCnt = pSpecialHistoryVo.getSpecial_cnt();
        this.goodCnt = pSpecialHistoryVo.getGood_cnt();
        this.listenerCnt = pSpecialHistoryVo.getListener_cnt();
        this.broadMin = pSpecialHistoryVo.getMinute_broadcast();
        this.roomNo = pSpecialHistoryVo.getRoom_no();
        this.isNew = pSpecialHistoryVo.getIsNew() == 1;
        this.fanYn = pSpecialHistoryVo.getFanYn();
    }
}
