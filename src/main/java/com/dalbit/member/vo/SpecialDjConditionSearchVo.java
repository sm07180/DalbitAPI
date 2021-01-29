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

}
