package com.dalbit.common.vo;

import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Random;

@Getter @Setter
public class FanBadgeVo implements Serializable {
    private String text;
    private String icon;
    private String startColor;
    private String endColor;

    public FanBadgeVo(String text, String icon, String startColor, String endColor){
        this.text = text;
        this.icon = icon;
        this.startColor = startColor;
        this.endColor = endColor;
    }
}
