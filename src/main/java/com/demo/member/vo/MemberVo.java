package com.demo.member.vo;

import com.demo.common.vo.BaseVo;
import com.demo.common.vo.ImageVo;
import com.demo.security.vo.SecurityUserVo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.awt.*;
import java.sql.Date;

@Getter @Setter
public class MemberVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    public static MemberVo getUserInfo() {
        //SecurityUserVo user = (SecurityUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (MemberVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    private String mem_no;
    private String nick_name;
    private String mem_sex;
    private int age;
    private String mem_id;
    private ImageVo background_image;
    private ImageVo profile_image;
    private String profile_msg;
    private int level;
    private int fan_count;
    private int star_count;
    private boolean enable_fan;

    private int idx;
    private String mem_phone;
    private String mem_passwd;
    private int mem_birth_year;
    private int mem_birth_month;
    private int mem_birth_day;
    private String mem_slct;
    private String mem_state;
    private Date mem_join_date;
    private Date last_upd_date;

    //TODO - 추후 DB에서 정보 추가
    private int exp = 50;
    private int expNext = 100;
    private String grade = "골드";
}
