package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum Item {

    애니_곰토끼(
            "1001"
            , "곰토끼"
            , "ani"
            , 10
            , "prop.item.1001.thumbs"
            , "prop.item.1001.webpUrl"
            , "prop.item.1001.lottieUrl"
            , ""
            , 600
            , 600
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    애니_곰인형(
            "1002"
            , "곰인형"
            , "ani"
            , 20
            , "prop.item.1002.thumbs"
            , "prop.item.1002.webpUrl"
            , "prop.item.1002.lottieUrl"
            , ""
            , 600
            , 600
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    애니_도너츠달(
            "1003"
            , "도너츠달"
            , "ani"
            , 30
            , "prop.item.1003.thumbs"
            , "prop.item.1003.webpUrl"
            , "prop.item.1003.lottieUrl"
            , ""
            , 600
            , 600
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    애니_도너츠(
            "1004"
            , "도너츠"
            , "ani"
            , 40
            , "prop.item.1004.thumbs"
            , "prop.item.1004.webpUrl"
            , "prop.item.1004.lottieUrl"
            , ""
            , 600
            , 600
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    스티커_곰(
            "1005"
            , "곰"
            , "sticker"
            , 3
            , "https://devimage.dalbitcast.com/ani/thumbs/sticker_1.png"
            , ""
            , ""
            , "https://devimage.dalbitcast.com/ani/sticker/sticker_1.png"
            , 0
            , 0
            , 0
            , 0
            , 0
            , "midLeft"
            , 10
    ),
    스티커_도너츠(
            "1006"
            , "도너츠"
            , "sticker"
            , 5
            , "https://devimage.dalbitcast.com/ani/thumbs/sticker_2.png"
            , ""
            , ""
            , "https://devimage.dalbitcast.com/ani/sticker/sticker_2.png"
            , 0
            , 0
            , 0
            , 0
            , 0
            , "midLeft"
            , 10
    )

   ;

    final private String itemNo;
    final private String itemNm;
    final private String type;
    final private int cost;
    final private String thumbs;
    final private String webpUrl;
    final private String lottieUrl;
    final private String stickerUrl;
    final private int width;
    final private int height;
    final private double deviceRate;
    final private double widthRate;
    final private double heightRate;
    final private String location;
    final private int duration;


    Item(String itemNo, String itemNm, String type, int cost, String thumbs, String webpUrl, String lottieUrl, String stickerUrl, int width, int height, double deviceRate, double widthRate, double heightRate, String location, int duration){
        this.itemNo = itemNo;
        this.itemNm = itemNm;
        this.type = type;
        this.cost = cost;
        this.thumbs = thumbs;
        this.webpUrl = webpUrl;
        this.lottieUrl = lottieUrl;
        this.stickerUrl = stickerUrl;
        this.width = width;
        this.height = height;
        this.deviceRate  = deviceRate;
        this.widthRate  = widthRate;
        this.heightRate  = heightRate;
        this.location  = location;
        this.duration = duration;
    }
}
