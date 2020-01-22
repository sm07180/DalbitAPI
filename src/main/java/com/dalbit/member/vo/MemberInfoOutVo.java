package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class MemberInfoOutVo {

    String  memNo;
    String  nickNm;
    String  gender;
    int     age;
    String  memId;
    ImageVo bgImg;
    ImageVo profImg;
    String  profMsg;
    int     level;
    int     fanCnt;
    int     starCnt;
    int     exp;
    int     expNext;
    String  grade;


    public MemberInfoOutVo(P_MemberInfoVo target, String target_mem_no) {
        this.memNo=target_mem_no;
        this.nickNm=target.getNickName();
        this.gender=target.getMemSex();
        this.age=target.getAge();
        this.memId=target.getMemId();
        this.bgImg=new ImageVo(target.getBackgroundImage(), DalbitUtil.getProperty("server.photo.url"));
        this.profImg=new ImageVo(target.getProfileImage(), DalbitUtil.getProperty("server.photo.url"));
        this.profMsg=target.getProfileMsg();
        this.level=target.getLevel();
        this.fanCnt=target.getFanCount();
        this.starCnt=target.getStarCount();
        this.exp=target.getExp();
        this.expNext=target.getExpNext();
        this.grade=target.getGrade();
    }
}
