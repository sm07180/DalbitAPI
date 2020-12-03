package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_SelfAuthVo extends P_ApiVo {

    private String mem_no;
    private String name;
    private String phoneNum;
    private String memSex;
    private String birthYear;
    private String birthMonth;
    private String birthDay;
    private String commCompany;
    private String foreignYN;
    private String certCode;

    private String parents_agreeTerm;
    private String parents_agreeDt;
    private String parents_agreeYn;

    private String os;
    private String isHybrid;
    private String returnUrl;
    private String pageCode;
    private String adultYn;
    private String authType;

    private String add_file;

    private String comment;

}
