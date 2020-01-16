package com.dalbit.member.vo;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Date;
import java.util.HashMap;

@Getter @Setter
public class MemberVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    public static MemberVo getUserInfo() {
        //SecurityUserVo user = (SecurityUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HashMap memberInfoMap = (HashMap)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MemberVo sessionMemberVo = new Gson().fromJson(DalbitUtil.getStringMap(memberInfoMap, "memberInfo"), MemberVo.class);

        return sessionMemberVo;
    }


    private String mem_no;
    private String mem_nick;
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
