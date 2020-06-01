package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProImageInitVo extends AdminBaseVo{

    private String mem_no;
    private String edit_contents;
    private String op_name;
    private String last_upd_date;
    private int type;
    private String image_profile;

}
