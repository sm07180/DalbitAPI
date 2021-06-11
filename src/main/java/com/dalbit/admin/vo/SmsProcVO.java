package com.dalbit.admin.vo;

import lombok.Data;

@Data
public class SmsProcVO {
    private String msgSlct;
    private String sendPhone;
    private String sendMemNo = "0";
    private String rcvPhone;
    private String rcvMemId = "";
    private String titleConts;
    private String msgBody;
    private String atchFile = "";
    private String rsrvDt = "";
    private int tranSlct;
    private int retVal;
}
