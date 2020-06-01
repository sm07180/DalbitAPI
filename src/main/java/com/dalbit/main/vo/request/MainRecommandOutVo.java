package com.dalbit.main.vo.request;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainRecommandVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainRecommandOutVo {

    /* Input */
    /*private String mem_no;*/

    /* OutPut */
    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private String roomNo;
    private String roomType;
    private String title;
    private String bannerUrl;
    private int listeners;
    private int likes;
    public boolean isSpecial;
}
