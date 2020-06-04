package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoVo {
    private String mem_no;
    private String mem_userid;
    private String mem_nick;
    private String mem_phone;
    private int level;
    private String grade;
}
