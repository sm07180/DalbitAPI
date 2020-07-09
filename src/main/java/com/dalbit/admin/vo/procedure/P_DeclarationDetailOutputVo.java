package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_DeclarationDetailOutputVo extends BaseVo {
    private int reportIdx;
    private int report_reason;
    private String etc;
    private String platform;
    private String browser;
    private String ipAddress;
    private String reg_date;
    private String op_date;
    private int status;
    private String op_name;
    private int op_code;
    private String mem_no;
    private String mem_id;
    private String mem_nick;
    private int level;
    private String grade;
    private String memSex;
    private int payCount;
    private int payAmount;
    private int giftCount;
    private int giftAmount;
    private String reported_mem_no;
    private String reported_mem_id;
    private String reported_mem_nick;
    private int reported_level;
    private String reported_grade;
    private String reported_memSex;
    private int reported_payCount;
    private int reported_payAmount;
    private int reported_giftCount;
    private int reported_giftAmount;
    private int chatCount;

}
