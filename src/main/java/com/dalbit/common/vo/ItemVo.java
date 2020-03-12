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
    private String stickerUrl;
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
        if(item.getThumbs().startsWith("https://")){
            this.thumbs = item.getThumbs();
        }else{
            this.thumbs = DalbitUtil.getProperty(item.getThumbs());
        }
        if(!DalbitUtil.isEmpty(item.getWebpUrl())) {
            if(item.getWebpUrl().startsWith("https://")){
                this.webpUrl = item.getWebpUrl();
            }else{
                this.webpUrl = DalbitUtil.getProperty(item.getWebpUrl());
            }
        }
        if(!DalbitUtil.isEmpty(item.getLottieUrl())) {
            if(item.getLottieUrl().startsWith("https://")) {
                this.lottieUrl = item.getLottieUrl();
            }else{
                this.lottieUrl = DalbitUtil.getProperty(item.getLottieUrl());
            }
        }
        if(!DalbitUtil.isEmpty(item.getWebpUrl())) {
            this.stickerUrl = item.getStickerUrl();
        }
        this.width = item.getWidth();
        this.height = item.getHeight();
        this.deviceRate = item.getDeviceRate();
        this.widthRate = item.getWidthRate();
        this.heightRate = item.getHeightRate();
        this.location = item.getLocation();
        this.duration = item.getDuration();
    }
}
