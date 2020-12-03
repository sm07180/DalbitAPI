package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MainStarVo extends P_ApiVo {
    private String memNo;
    private String nickNm;
    private String gender;
    private String profImgUrl;
    private String roomNo;
    private String title;
    private String roomType;
    private String roomTypeNm;
}
