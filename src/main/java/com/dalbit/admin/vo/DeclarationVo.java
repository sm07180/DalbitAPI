package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeclarationVo extends AdminBaseVo{

    private String opName;
    private String mem_no;
    private String mem_userid;
    private String mem_nick;
    private String ip;
    private String browser;
    private String flatform;
    private String reported_mem_no;
    private String reported_userid;
    private String reported_nick;
    private String reported_phone;
    private int reported_level;
    private String reported_grade;
    private int opCode;
    private String notiContents;
    private String notiMemo;
    private int blockDay;

}
