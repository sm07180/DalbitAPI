package com.dalbit.main.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerVo {
    private long idx;
    private String thumbsUrl;
    private String linkUrl;
    private String linkType;
    private String title;
    private String bannerUrl;
    private int popup_type;
    private int is_cookie;
    private String contents;
}
