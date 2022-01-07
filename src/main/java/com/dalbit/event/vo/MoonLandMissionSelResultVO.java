package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoonLandMissionSelResultVO {
     private Integer dalCnt;
     private String img;
     private String itemNo;
     private String itemNm;
     private String completed;
     private Integer sortKey;

    public MoonLandMissionSelResultVO(Integer dalCnt, String img, String itemNo, String itemNm, String completed, Integer sortKey) {
        this.dalCnt = dalCnt;
        this.img = img;
        this.itemNo = itemNo;
        this.itemNm = itemNm;
        this.completed = completed;
        this.sortKey = sortKey;
    }
}
