package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class SpecialDjConditionVo {

    private int already;
    private int timeState; //0:지원기간 아님, 1:지원가능

   /* private int condition1;
    private int condition2;
    private int condition3;

    private String conditionTitle1;
    private String conditionTitle2;
    private String conditionTitle3;

    private String conditionValue1;
    private String conditionValue2;
    private String conditionValue3;*/

    private ArrayList<HashMap<String,String>> conditionList;

}
