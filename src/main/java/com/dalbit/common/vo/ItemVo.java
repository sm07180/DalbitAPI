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
    private String url;

    public ItemVo(String itemNo, String itemNm, String type, int cost, String thumbs, String url){
        this.itemNm = itemNm;
        this.itemNo = itemNo;
        this.thumbs = thumbs;
        this.cost = cost;
        this.type = type;
        this.url = url;
    }
}
