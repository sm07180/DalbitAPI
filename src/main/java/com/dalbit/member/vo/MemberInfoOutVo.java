package com.dalbit.member.vo;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

@Getter @Setter
@ToString
public class MemberInfoOutVo {

    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    MessageUtil messageUtil;

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
    int rubyCnt;
    int goldCnt;
    Object  fanRank1;                      //팬랭킹1위 정보
    Object  fanRank2;                      //팬랭킹2위 정보
    Object  fanRank3;                      //팬랭킹3위 정보


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
        this.rubyCnt=Integer.valueOf(target.getRuby());
        this.goldCnt=Integer.valueOf(target.getGold());
        this.fanRank1=new Gson().fromJson(target.getFanRank1(), Object.class);
        this.fanRank2=new Gson().fromJson(target.getFanRank2(), Object.class);
        this.fanRank3=new Gson().fromJson(target.getFanRank3(), Object.class);
    }
}
