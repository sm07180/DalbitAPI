package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProImageInitVo extends AdminBaseVo{

    private String mem_no;
    private String mem_nick;
    private String edit_contents;
    private String op_name;
    private String last_upd_date;
    private int type;
    private String image_profile;
    private String reset_image_profile;

    private String report_title;
    private String report_message;

}
