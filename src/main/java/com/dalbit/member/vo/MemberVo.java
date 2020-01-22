package com.dalbit.member.vo;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Date;

@Getter
@Setter
public class MemberVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    public static MemberVo getUserInfo() {
        //SecurityUserVo user = (SecurityUserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //HashMap memberInfoMap = (HashMap)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //MemberVo sessionMemberVo = new Gson().fromJson(DalbitUtil.getStringMap(memberInfoMap, "memberInfo"), MemberVo.class);

        String memNo = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MemberVo memberVo = new MemberVo();
        memberVo.setMemNo(memNo);
        return memberVo;
    }

    private String memNo;
    private String memNick;
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

    private int exp;
    private int expNext;
    private String grade;
}
