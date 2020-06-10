package com.dalbit.store.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StoreChargeVo {
    private String itemNo;
    private String itemNm;
    private String img;
    private String itemType;
    private int itemPrice;
    private int discountRate;
    private int salePrice;
    private String iosPrice = "";
    private int givenDal;
    private int itemPriceDefault;
    private int itemPriceIos;
}
