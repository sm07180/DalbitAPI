package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_ItemVo extends P_ApiVo {
    private int item_slct;
    private String platform;
    private String booster;
    private String levelUp;
    private String direct;
    private String[] directs;
    private String[] visibilityDirects;
}
