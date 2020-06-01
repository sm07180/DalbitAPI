package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NickTextInitVo extends AdminBaseVo{

    private String mem_no;
    private String edit_contents;
    private String op_name;
    private String last_upd_date;
    private int type;
    private String mem_userid;
    private String mem_nick;

}
