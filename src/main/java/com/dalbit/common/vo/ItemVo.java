package com.dalbit.common.vo;

import com.dalbit.common.code.Item;
import com.dalbit.util.DalbitUtil;
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
    private int width;
    private int height;
    private double deviceRate;
    private double widthRate;
    private double heightRate;
    private String location; // topLeft bottomRight

    public ItemVo(Item item){
        this.itemNm = item.getItemNm();
        this.itemNo = item.getItemNo();
        this.cost = item.getCost();
        this.thumbs = DalbitUtil.getProperty(item.getThumbs());
        this.webpUrl = DalbitUtil.getProperty(item.getWebpUrl());
        this.lottieUrl = DalbitUtil.getProperty(item.getLottieUrl());
        this.width = item.getWidth();
        this.height = item.getHeight();
        this.deviceRate = item.getDeviceRate();
        this.widthRate = item.getWidthRate();
        this.heightRate = item.getHeightRate();
        this.location = item.getLocation();
    }
}
