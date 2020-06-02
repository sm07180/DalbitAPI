package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BroTitleTextInitVo extends AdminBaseVo{

    private String room_no;
    private String op_name;
    private String title;
    private String mem_nick;
    private String reset_title;

}
