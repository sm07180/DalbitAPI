package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateStateVo extends AdminBaseVo{

    private String mem_no;
    private int state;
    private int opCode;
    private int blockDay;
    private String last_upd_date;

}
