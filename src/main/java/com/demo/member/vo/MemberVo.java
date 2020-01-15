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


    private String memNo;
    private String nickName;
    private String memSex;
    private int age;
    private String memId;
    private ImageVo backgroundImage;
    private ImageVo profileImage;
    private String profileMsg;
    private int level;
    private int fanCount;
    private int starCount;
    private boolean enableFan;

    //TODO - 삭제 확인
    private int idx;
    private String memPhone;
    private String memPasswd;
    private int memBirthYear;
    private int memBirthMonth;
    private int memBirthDay;
    private String memSlct;
    private String memState;
    private Date memJoinDate;
    private Date lastUpdDate;
}
