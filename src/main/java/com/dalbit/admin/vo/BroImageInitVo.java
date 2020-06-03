package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BroImageInitVo extends AdminBaseVo{

    private String room_no;
    private String mem_no;
    private String edit_contents;
    private String op_name;
    private String last_upd_date;
    private String image_background;
    private String reset_image_background;

    private String report_title;
    private String report_message;

}
