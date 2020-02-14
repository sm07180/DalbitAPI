package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVo {
    private String itemNo;
    private String itemNm;
    private int cost;
    private String thumbs;
    private String webpUrl;
    private String lottieUrl;
    private int width = 600;
    private int height = 600;

    public ItemVo(String itemNo, String itemNm, int cost, String thumbs, String webpUrl, String lottieUrl){
        this.itemNm = itemNm;
        this.itemNo = itemNo;
        this.thumbs = thumbs;
        this.cost = cost;
        this.webpUrl = webpUrl;
        this.lottieUrl = lottieUrl;
    }
}
