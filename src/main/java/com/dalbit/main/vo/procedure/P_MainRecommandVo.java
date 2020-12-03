package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MainRecommandVo extends P_ApiVo {

    private String paramMemNo;
    private String paramDevice;
    private String paramPlanMemNo;
    private String paramPlatform;
    private int paramIsWowza;

    private String memNo;
    private String nickNm;
    private String gender;
    private String profileUrl;
    private String roomNo;
    private String roomType;
    private String title;
    private int listeners;
    private int likes;
    private String bannerUrl;
    public boolean isSpecial;
    public boolean isAdmin = false;
    public boolean isNew = false;
    public int isWowza;

    public String liveBadgeText;
    public String liveBadgeIcon;
    public String liveBadgeStartColor;
    public String liveBadgeEndColor;
    public String liveBadgeImage;
    public String liveBadgeImageSmall;
}
