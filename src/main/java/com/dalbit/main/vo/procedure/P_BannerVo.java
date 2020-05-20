package com.dalbit.main.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_BannerVo {
    private String paramMemNo;
    private String paramDevice;
    private String paramPlatform;
    private String paramPosition;

    private long idx;
    private String thumbsUrl;
    private String linkUrl;
    private String linkType;
    private String title;
    private String bannerUrl;
}
