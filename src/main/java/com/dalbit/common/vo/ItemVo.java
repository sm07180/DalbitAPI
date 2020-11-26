package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVo {
    private String itemNo;
    private String itemNm;
    private String type;
    private int cost;
    private String thumbs;
    private String webpUrl;
    private String webpFileName;
    private String lottieUrl;
    private String lottieFileName;
    private String soundFileUrl;
    private String soundFileName;
    private int width;
    private int height;
    private double deviceRate;
    private int widthRate;
    private double heightRate;
    private String location;
    private int duration;
    private String iosSelectType = "lottie"; //webp, lottie
    private String category;
    private boolean visibility = true;
    private boolean isNew;
    private int sortNo;
}
