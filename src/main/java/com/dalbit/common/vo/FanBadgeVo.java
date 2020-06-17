package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Random;

@Getter @Setter
public class FanBadgeVo implements Serializable {
    private String text;
    private String icon;
    private String startColor;
    private String endColor;

    public FanBadgeVo(){
        Random random = new Random();
        int rndNumber = random.nextInt(4);
        if(rndNumber == 1) {
            this.text = "회장님";
            this.icon = "https://image.dalbitlive.com/badge/200617/fanbadge_01.png";
            this.startColor = "#fe8fbe";
            this.endColor = "#f82cad";
        }else if(rndNumber == 2){
            this.text = "부회장님";
            this.icon = "https://image.dalbitlive.com/badge/200617/fanbadge_02.png";
            this.startColor = "#7210ff";
            this.endColor = "#817dff";
        }else if(rndNumber == 3){
            this.text = "사장님";
            this.icon = "https://image.dalbitlive.com/badge/200617/fanbadge_03.png";
            this.startColor = "#ffc400";
            this.endColor = "#ff7010";
        }
    }

    public FanBadgeVo(int type){
        if(type == 1) {
            this.text = "회장님";
            this.icon = "https://image.dalbitlive.com/badge/200617/fanbadge_01.png";
            this.startColor = "#fe8fbe";
            this.endColor = "#f82cad";
        }else if(type == 2){
            this.text = "부회장님";
            this.icon = "https://image.dalbitlive.com/badge/200617/fanbadge_02.png";
            this.startColor = "#7210ff";
            this.endColor = "#817dff";
        }else if(type == 3){
            this.text = "사장님";
            this.icon = "https://image.dalbitlive.com/badge/200617/fanbadge_03.png";
            this.startColor = "#ffc400";
            this.endColor = "#ff7010";
        }
    }
}
