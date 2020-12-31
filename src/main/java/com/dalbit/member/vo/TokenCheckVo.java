package com.dalbit.member.vo;

import lombok.Setter;
import lombok.Getter;

import java.sql.Date;

@Getter
@Setter
public class TokenCheckVo {
    private String idx;
    private String mem_no;
    private String mem_id;
    private String mem_passwd;
    private String mem_phone;
    private String mem_nick;
    private String mem_userid;
    private String mem_name;
    private String mem_email;
    private int foreign_yn;
    private String mem_sex;
    private int mem_birth_year;
    private int mem_birth_month;
    private int mem_birth_day;
    private String mem_slct;
    private String mem_adid;
    private int mem_state;
    private int block_day;
    private Date block_end_date;
    private Date mem_join_date;
    private int os_type;
    private String location;
    private String ip;
    private Date last_upd_date;
    private int isAdmin;
}
