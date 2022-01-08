package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaySuccSelVo {
    private String order_id;
    private String mem_no;
    private String pay_way;
    private String pay_amt;
    private String item_amt;
    private String pay_code;
    private String card_no;
    private String card_nm;
    private String pay_ok_date;
    private String pay_ok_time;
}
