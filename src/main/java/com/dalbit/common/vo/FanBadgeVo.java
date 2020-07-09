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
}
