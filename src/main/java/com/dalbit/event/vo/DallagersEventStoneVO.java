package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DallagersEventStoneVO {
    private Integer dStone = 0;
    private Integer aStone = 0;
    private Integer lStone = 0;

    public DallagersEventStoneVO(){}

    public DallagersEventStoneVO(Integer dStone, Integer aStone, Integer lStone){
        this.dStone = dStone;
        this.aStone = aStone;
        this.lStone = lStone;
    }
}
