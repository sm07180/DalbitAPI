package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FanBadgeVo {
    private String text;
    private String icon;
    private String startColor;
    private String endColor;
    private String image;
    private String imageSmall;
    private String frameTop;
    private String frameChat;
    private String frameAni;
    private String tipMsg;
    private String explainMsg;
    private String enterAni;
    private String enterBgImg;
    private String msgBorderSrtColor;
    private String msgBorderEndColor;

    public FanBadgeVo(String text, String icon, String startColor, String endColor){
        this.text = text;
        this.icon = icon;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public FanBadgeVo(String text, String icon, String startColor, String endColor, String image){
        this.text = text;
        this.icon = icon;
        this.startColor = startColor;
        this.endColor = endColor;
        this.image = image;
    }

    public FanBadgeVo(String text, String icon, String startColor, String endColor, String image, String imageSmall){
        this.text = text;
        this.icon = icon;
        this.startColor = startColor;
        this.endColor = endColor;
        this.image = image;
        this.imageSmall = imageSmall;
    }

    public FanBadgeVo(String text, String icon, String startColor, String endColor, String image, String imageSmall, String frameTop, String frameChat, String frameAni, String tipMsg, String explainMsg, String enterAni, String enterBgImg, String msgBorderSrtColor, String msgBorderEndColor){
        this.text = text;
        this.icon = icon;
        this.startColor = startColor;
        this.endColor = endColor;
        this.image = image;
        this.imageSmall = imageSmall;
        this.frameTop = frameTop;
        this.frameChat = frameChat;
        this.frameAni = frameAni;
        this.tipMsg = tipMsg;
        this.explainMsg = explainMsg;
        this.enterAni = enterAni;
        this.enterBgImg = enterBgImg;
        this.msgBorderSrtColor = msgBorderSrtColor;
        this.msgBorderEndColor = msgBorderEndColor;
    }
}
