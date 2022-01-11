package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GoodStartInfoVo {
    private String good_no;
    private String start_date;
    private Date end_date;
    private Date ins_date;
}
