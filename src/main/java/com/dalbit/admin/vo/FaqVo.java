package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class FaqVo {

    private int idx;
    private int slct_type;
    private String question;
    private String answer;
    private String op_name;
    private int view_on;
    private int view_count;
    private Date reg_date;
    private Date last_upd_date;

}
