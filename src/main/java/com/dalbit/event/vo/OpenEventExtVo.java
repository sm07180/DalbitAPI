package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OpenEventExtVo {

    private int myRank;
    private int myPoint;
    private int diffPoint;
    private int opRank;
    private String startDate;
    private String endDate;
    private String detailDesc;
    private String giftDesc;
    private List list;

}
