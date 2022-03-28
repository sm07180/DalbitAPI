package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.common.vo.ResVO;
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
    private String parent_agreeYn;

    private String os;
    private String isHybrid;
    private String returnUrl;
    private String pageCode;
    private String adultYn;
    private String authType;

    private String add_file;
    private String comment;
    private String authToken;

    private String agreePeriod = "0";
    private ResVO parentAuth; // -5:부모미성년, -4:미인증, -3:나이 안맞음, -2:이메일 미등록, -1:이미 동의된 데이터, 0:에러, 1:정상

}
