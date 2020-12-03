package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BadgeVo {
    private String text;
    private String icon;
    private String startColor;
    private String endColor;
    private String textColor = "#ffffff";
    private String borderColor = "";
    private float bgAlpha = (float)1.0;
    private String bgImg = "";
    private String tipMsg;
    private String explainMsg;

    public BadgeVo(){}
    public BadgeVo(FanBadgeVo fanBadgeVo){
        this.text = fanBadgeVo.getText();
        this.icon = fanBadgeVo.getIcon();
        this.startColor = fanBadgeVo.getStartColor();
        this.endColor = fanBadgeVo.getEndColor();
        this.textColor = fanBadgeVo.getTextColor();
        this.borderColor = fanBadgeVo.getBorderColor();
        this.bgAlpha = fanBadgeVo.getBgAlpha();
        this.bgImg = fanBadgeVo.getBgImg();
        this.tipMsg = fanBadgeVo.getTipMsg();
        this.explainMsg = fanBadgeVo.getExplainMsg();
    }
}
