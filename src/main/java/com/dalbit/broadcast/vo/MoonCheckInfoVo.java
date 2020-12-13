package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MoonCheckInfoVo {
    private int moonStep;
    private String moonStepFileNm;
    private String moonStepAniFileNm;
    private String dlgTitle;
    private String dlgText;
    private int aniDuration;
}
