package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeclarationVo extends AdminBaseVo{

    private int reportIdx;
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
    private String notiMemo = "";
    private int blockDay;
    private String etc;
    private String last_upd_date;
    private int state;
    private int status;

    /* 차단 기능을 위해 추가 */
    private Integer uuid_block;
    private Integer ip_block;
    private String block_text;
    private int block_day;
    private int block_type;
    private String block_end_date;
    private String adminMemo;
    private String device_uuid;

    /* history를 위한 */
    private String edit_contents;
    private int edit_type;
}
