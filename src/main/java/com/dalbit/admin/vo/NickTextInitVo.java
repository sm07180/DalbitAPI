package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NickTextInitVo extends AdminBaseVo{

    private String mem_no;
    private String op_name;
    private String mem_userid;
    private String mem_nick;

    private String report_title;
    private String report_message;
}
