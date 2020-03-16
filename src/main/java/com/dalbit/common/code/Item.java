package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum Item {

    스티커_게(
            "1001"
            , "게"
            , "sticker"
            , 10
            , "https://devimage.dalbitcast.com/ani/thumbs/200316/item_hermitcrab_thumbs_200316.png"
            , "https://devimage.dalbitcast.com/ani/webp/200316/item_hermitcrab_200316.webp"
            , "https://devimage.dalbitcast.com/ani/lottie/200316/item_hermitcrab_200316.json"
            , 108
            , 108
            , 1
            , 1
            , 1
            , "midLeft"
            , 3
    ),
    스티커_사브르 (
            "1002"
            , "사브르 "
            , "sticker"
            , 30
            , "https://devimage.dalbitcast.com/ani/thumbs/200316/item_UFO_thumbs_200316.png"
            , "https://devimage.dalbitcast.com/ani/webp/200316/item_UFO_200316.webp"
            , "https://devimage.dalbitcast.com/ani/lottie/200316/item_lightsaber_200316.json"
            , 108
            , 108
            , 1
            , 1
            , 1
            , "midLeft"
            , 3
    ),
    애니_파이어웍(
            "1003"
            , "파이어웍"
            , "ani"
            , 50
            , "https://devimage.dalbitcast.com/ani/thumbs/200316/item_firework_thumbs_200316.png"
            , "https://devimage.dalbitcast.com/ani/webp/200316/item_firework_200316.webp"
            , "https://devimage.dalbitcast.com/ani/lottie/200316/item_firework_200316.json"
            , 720
            , 504
            , 1
            , 1
            , 0.7
            , "topLeft"
            , 10
    ),
    애니_토끼(
            "1004"
            , "토끼"
            , "ani"
            , 70
            , "https://devimage.dalbitcast.com/ani/thumbs/200316/item_rabbit_thumbs_200316.png"
            , "https://devimage.dalbitcast.com/ani/webp/200316/item_rabbit_200316.webp"
            , "https://devimage.dalbitcast.com/ani/lottie/200316/item_rabbit_200316.json"
            , 360
            , 900
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    애니_로켓(
            "1005"
            , "로켓"
            , "ani"
            , 100
            , "https://devimage.dalbitcast.com/ani/thumbs/200316/item_roket_thumbs_200316.png"
            , "https://devimage.dalbitcast.com/ani/webp/200316/item_rocket_200316.webp"
            , "https://devimage.dalbitcast.com/ani/lottie/200316/item_rocket_200316.json"
            , 360
            , 900
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    애니_UFO(
            "1006"
            , "UFO"
            , "ani"
            , 200
            , "https://devimage.dalbitcast.com/ani/thumbs/200316/item_UFO_thumbs_200316.png"
            , "https://devimage.dalbitcast.com/ani/webp/200316/item_UFO_200316.webp"
            , "https://devimage.dalbitcast.com/ani/lottie/200316/item_UFO_200316.json"
            , 720
            , 504
            , 1
            , 1
            , 0.7
            , "topLeft"
            , 10
    ),

    파티클_1(
            "9001"
            , "파티클_1"
            , "particle"
            , 0
            , ""
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart1_20200313.webp"
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart1_20200313.json"
            , 360
            , 900
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    파티클_2(
            "9002"
            , "파티클_2"
            , "particle"
            , 0
            , ""
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart2_20200313.webp"
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart2_20200313.json"
            , 360
            , 900
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    파티클_3(
            "9003"
            , "파티클_3"
            , "particle"
            , 0
            , ""
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart3_20200313.webp"
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart3_20200313.json"
            , 360
            , 900
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    파티클_4(
            "9004"
            , "파티클_4"
            , "particle"
            , 0
            , ""
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart4_20200313.webp"
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart4_20200313.json"
            , 360
            , 900
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
            , 10
    ),
    파티클_5(
            "9005"
            , "파티클_5"
            , "particle"
            , 0
            , ""
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart5_20200313.webp"
            , "https://devimage.dalbitcast.com/ani/likeparticle/heart5_20200313.json"
            , 360
            , 900
            , 0.5
            , 1
            , 2.5
            , "bottomRight"
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
    final private int width;
    final private int height;
    final private double deviceRate;
    final private double widthRate;
    final private double heightRate;
    final private String location;
    final private int duration;


    Item(String itemNo, String itemNm, String type, int cost, String thumbs, String webpUrl, String lottieUrl, int width, int height, double deviceRate, double widthRate, double heightRate, String location, int duration){
        this.itemNo = itemNo;
        this.itemNm = itemNm;
        this.type = type;
        this.cost = cost;
        this.thumbs = thumbs;
        this.webpUrl = webpUrl;
        this.lottieUrl = lottieUrl;
        this.width = width;
        this.height = height;
        this.deviceRate  = deviceRate;
        this.widthRate  = widthRate;
        this.heightRate  = heightRate;
        this.location  = location;
        this.duration = duration;
    }
}
