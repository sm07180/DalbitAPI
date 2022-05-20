package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ElectricSignDJListVo {
    private Integer pageNo;
    private String memNo;
    private Integer pagePerCnt;

    private ImageVo profImg;

}
