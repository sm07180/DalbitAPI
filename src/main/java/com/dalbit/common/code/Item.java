package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum Item {

    곰토끼(
            "1001"
            , "곰토끼"
            , 10
            , "prop.item.1001.thumbs"
            , "prop.item.1001.webpUrl"
            , "prop.item.1001.lottieUrl"
            , 600
            , 600
    ),


    곰인형(
            "1002"
            , "곰인형"
            , 20
            , "prop.item.1002.thumbs"
            , "prop.item.1002.webpUrl"
            , "prop.item.1002.lottieUrl"
            , 600
            , 600
    ),

    도너츠달(
            "1003"
            , "도너츠달"
            , 30
            , "prop.item.1003.thumbs"
            , "prop.item.1003.webpUrl"
            , "prop.item.1003.lottieUrl"
            , 600
            , 600
    ),

    도너츠(
            "1004"
            , "도너츠"
            , 40
            , "prop.item.1004.thumbs"
            , "prop.item.1004.webpUrl"
            , "prop.item.1004.lottieUrl"
            , 600
            , 600
    ),

   ;

    final private String itemNo;
    final private String itemNm;
    final private int cost;
    final private String thumbs;
    final private String webpUrl;
    final private String lottieUrl;
    final private int width;
    final private int height;

    Item(String itemNo, String itemNm, int cost, String thumbs, String webpUrl, String lottieUrl, int width, int height){
        this.itemNo = itemNo;
        this.itemNm = itemNm;
        this.cost = cost;
        this.thumbs = thumbs;
        this.webpUrl = webpUrl;
        this.lottieUrl = lottieUrl;
        this.width = width;
        this.height = height;
    }
}
