package com.dalbit.member.vo;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProfileInfoOutVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String  memNo;
    private String  nickNm;
    private String  gender;
    private int     age;
    private String  memId;
    private ImageVo bgImg;
    private ImageVo profImg;
    private String  profMsg;
    private int     level;
    private int     fanCnt;
    private int     starCnt;
    private int     isFan;
    private int     exp;
    private int     expNext;
    private String  grade;
    private int     rubyCnt;
    private int     goldCnt;
    List fanRank;

    public ProfileInfoOutVo(){}
    public ProfileInfoOutVo(P_ProfileInfoVo target, String target_mem_no, List fanRank) {
        this.memNo = target_mem_no;
        this.nickNm = target.getNickName();
        this.gender = target.getMemSex();
        this.age = target.getAge();
        this.memId = target.getMemId();
        this.bgImg = new ImageVo(target.getBackgroundImage(), DalbitUtil.getProperty("server.photo.url"));
        this.profImg = new ImageVo(target.getProfileImage(), DalbitUtil.getProperty("server.photo.url"));
        this.profMsg = target.getProfileMsg();
        this.level = target.getLevel();
        this.fanCnt = target.getFanCount();
        this.starCnt = target.getStarCount();
        this.isFan = target.getEnableFan();
        if(target_mem_no.equals(target.getMem_no())){
            this.exp = target.getExp();
            this.expNext = target.getExpNext();
            this.rubyCnt = target.getRuby() == null ? 0 : Integer.valueOf(target.getRuby());
            this.goldCnt = target.getGold() == null ? 0 : Integer.valueOf(target.getGold());
        }else{
            this.exp = 0;
            this.expNext = 0;
            this.rubyCnt = 0;
            this.goldCnt = 0;
        }
        this.grade = target.getGrade();
        this.fanRank = fanRank;
    }
}
