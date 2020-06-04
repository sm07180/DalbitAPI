package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeclarationVo extends AdminBaseVo{

    private int reportIdx;
    private int opCode;
    private int sendNoti;
    private String notiContents;
    private String notiMemo;
    private String opName;

}
