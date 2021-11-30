package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpecialDjConditionSearchVo {

    private int slct_type = 0;//0:시간, 1:분, 2:초
    private String mem_no;
    private int condition_code;
    private int condition_data;
    private String condition_start_date;
    private String condition_end_date;

    private int airtime_type = 1; //0:팬방송제외,  1:전체

    public void setCondition_code(int condition_code) {
        //8번은 팬방송 제외
        if(condition_code == 8){
            setAirtime_type(0);
        }
        this.condition_code = condition_code;
    }
}
