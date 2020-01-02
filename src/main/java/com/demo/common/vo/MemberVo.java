package com.demo.common.vo;

import com.demo.security.vo.SecurityUserVo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter @Setter
public class MemberVo extends BaseVo {

    public static UserVo getUserInfo() {
        SecurityUserVo user = (SecurityUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserInfo();
    }

    private int mem_no;
    private String mem_id;
    private String mem_phone;
    private String mem_password;
    private String mem_nick;
    private String mem_sex;
    private int mem_birth_year;
    private int mem_birth_month;
    private String mem_slct;
    private String mem_state;
}
