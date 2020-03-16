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
    private String type;
    private int cost;
    private String thumbs;
    private String webpUrl;
    private String lottieUrl;
    private int width;
    private int height;
    private double deviceRate;
    private double widthRate;
    private double heightRate;
    private String location;
    private int duration;

    public ItemVo(Item item){
        this.itemNo = item.getItemNo();
        this.itemNm = item.getItemNm();
        this.type = item.getType();
        this.cost = item.getCost();
        this.thumbs = item.getThumbs();
        this.webpUrl = item.getWebpUrl();
        this.lottieUrl = item.getLottieUrl();
        this.width = item.getWidth();
        this.height = item.getHeight();
        this.deviceRate = item.getDeviceRate();
        this.widthRate = item.getWidthRate();
        this.heightRate = item.getHeightRate();
        this.location = item.getLocation();
        this.duration = item.getDuration();
    }
}
