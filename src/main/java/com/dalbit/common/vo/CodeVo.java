package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeVo {
    private String cd;
    private String cdNm;
    private String value;
    private int sortNo;
    private int isUse;

    public CodeVo(){}

    public CodeVo(String type){
        this.cd = type;
    }

    public CodeVo(String type, String code){
        this.cd = type;
        this.cdNm = code;
    }

    public CodeVo(String cd, String cdNm, int sortNo, int isUse){
        this.cd = cd;
        this.cdNm = cdNm;
        this.sortNo = sortNo;
        this.isUse = isUse;
    }

    public CodeVo(String type, String value, String cdNm, int sortNo, int isUse){
        this.cd = type;
        this.value = value;
        this.cdNm = cdNm;
        this.sortNo = sortNo;
        this.isUse = isUse;
    }
}
