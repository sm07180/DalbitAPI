package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeVo {
    private String cd;
    private String cdNm;
    private int sortNo;
    private int isUse;

    public CodeVo(String cd, String cdNm, int sortNo, int isUse){
        this.cd = cd;
        this.cdNm = cdNm;
        this.sortNo = sortNo;
        this.isUse = isUse;
    }
}
