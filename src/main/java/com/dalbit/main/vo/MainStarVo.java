package com.dalbit.main.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MainStarVo {
    private String memNo;
    private String nickNm;
    private String gender;
    private String roomNo;
    private String title;
    private String roomType;
    private String roomTypeNm;
    private ImageVo profImg;
}
