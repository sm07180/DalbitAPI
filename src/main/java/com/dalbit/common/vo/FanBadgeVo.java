package com.dalbit.common.vo;

import com.dalbit.util.DalbitUtil;
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

    private String textColor = "#ffffff";
    private String borderColor = "";
    private float bgAlpha = (float)1.0;
    private String bgImg = "";

    private String chatImg;
    private int chatImgWidth;
    private int chatImgHeight;

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

    public FanBadgeVo(String text, String icon, String startColor, String endColor, String textColor, String borderColor, float bgAlpha, String bgImg, String frameTop, String frameChat, String frameAni, String tipMsg, String explainMsg, String enterAni, String enterBgImg, String msgBorderSrtColor, String msgBorderEndColor){
        this.text = text;
        this.icon = icon;
        this.startColor = startColor;
        this.endColor = endColor;
        this.frameTop = frameTop;
        this.frameChat = frameChat;
        this.frameAni = frameAni;
        this.tipMsg = tipMsg;
        this.explainMsg = explainMsg;
        this.enterAni = enterAni;
        this.enterBgImg = enterBgImg;
        this.msgBorderSrtColor = msgBorderSrtColor;
        this.msgBorderEndColor = msgBorderEndColor;

        this.textColor = textColor;
        this.borderColor = borderColor;
        this.bgAlpha = bgAlpha;
        this.bgImg = bgImg;
    }

    public FanBadgeVo(String text, String icon, String startColor, String endColor, String textColor, String borderColor, float bgAlpha, String bgImg, String frameTop, String frameChat, String frameAni, String tipMsg, String explainMsg, String enterAni, String enterBgImg, String msgBorderSrtColor, String msgBorderEndColor, String chatImg, int chatImgWidth, int chatImgHeight){
        this.text = text;
        this.icon = icon;
        this.startColor = startColor;
        this.endColor = endColor;
        this.frameTop = frameTop;
        this.frameChat = frameChat;
        this.frameAni = frameAni;
        this.tipMsg = tipMsg;
        this.explainMsg = explainMsg;
        this.enterAni = enterAni;
        this.enterBgImg = enterBgImg;
        this.msgBorderSrtColor = msgBorderSrtColor;
        this.msgBorderEndColor = msgBorderEndColor;

        this.textColor = textColor;
        this.borderColor = borderColor;
        this.bgAlpha = bgAlpha;
        this.bgImg = bgImg;
        this.chatImg = DalbitUtil.isEmpty(chatImg) ? "" : chatImg;
        this.chatImgWidth = chatImgWidth;
        this.chatImgHeight = chatImgHeight;
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

    public FanBadgeVo(String text, String icon, String startColor, String endColor, String image, String imageSmall, String tipMsg, String explainMsg, String enterAni, String enterBgImg, String msgBorderSrtColor, String msgBorderEndColor){
        this.text = text;
        this.icon = icon;
        this.startColor = startColor;
        this.endColor = endColor;
        this.image = image;
        this.imageSmall = imageSmall;
        this.tipMsg = tipMsg;
        this.explainMsg = explainMsg;
        this.enterAni = enterAni;
        this.enterBgImg = enterBgImg;
        this.msgBorderSrtColor = msgBorderSrtColor;
        this.msgBorderEndColor = msgBorderEndColor;
    }
}
