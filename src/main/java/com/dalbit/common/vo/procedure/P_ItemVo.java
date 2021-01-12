package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.google.gson.Gson;
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
    private String jsonDirects;
    private String jsonVisibilityDirects;
    private String itemCode;

    public void setDirects(String[] directs){
        this.directs = directs;
        this.jsonDirects = new Gson().toJson(this.directs);
    }

    public void setVisibilityDirects(String[] visibilityDirects){
        this.visibilityDirects = visibilityDirects;
        this.jsonVisibilityDirects = new Gson().toJson(this.visibilityDirects);
    }
}
