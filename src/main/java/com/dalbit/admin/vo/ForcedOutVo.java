package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ForcedOutVo extends AdminBaseVo{

    private String room_no;
    private String mem_no;
    private String sendNoti;
    private String notiContents = "";
    private String notiMemo = "";

    private String mem_nick;
    private String report_title;
    private String report_message;
    private String dj_mem_no;
    private String dj_nickname;

    private String opName;

}
