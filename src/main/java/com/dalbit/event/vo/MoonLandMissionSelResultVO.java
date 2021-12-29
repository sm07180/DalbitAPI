package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoonLandMissionSelResultVO {
     private String dalCnt;
     private String img;
     private String itemNo;
     private String itemNm;
     private String completed;

    public MoonLandMissionSelResultVO(String dalCnt, String img, String itemNo, String itemNm, String completed) {
        this.dalCnt = dalCnt;
        this.img = img;
        this.itemNo = itemNo;
        this.itemNm = itemNm;
        this.completed = completed;
    }
}
