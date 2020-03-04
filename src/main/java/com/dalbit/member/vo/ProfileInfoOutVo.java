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
    private ImageVo profImg;
    private String  profMsg;
    private int     level;
    private int     fanCnt;
    private int     starCnt;
    private int     isFan;
    private int     exp;
    private int     expBegin;
    private int     expNext;
    private String  grade;
    private int     dalCnt;
    private int     byeolCnt;
    private Boolean isRecomm;
    private Boolean isPop;
    private Boolean isNew;
    private String roomNo;
    private long broadTotTime;
    private long listenTotTime;
    private int likeTotCnt;
    List fanRank;

    public ProfileInfoOutVo(){}
    public ProfileInfoOutVo(P_ProfileInfoVo target, String target_mem_no, List fanRank) {
        this.memNo = target_mem_no;
        this.nickNm = target.getNickName();
        this.gender = target.getMemSex();
        this.age = target.getAge();
        this.memId = target.getMemId();
        this.profImg = new ImageVo(target.getProfileImage(), DalbitUtil.getProperty("server.photo.url"));
        this.profMsg = target.getProfileMsg();
        this.level = target.getLevel();
        this.fanCnt = target.getFanCount();
        this.starCnt = target.getStarCount();
        this.isFan = target.getEnableFan();
        this.roomNo = target.getRoom_no();

        if(target_mem_no.equals(target.getMem_no())){
            this.exp = target.getExp();
            this.expBegin = target.getExpBegin();
            this.expNext = target.getExpNext();
            this.dalCnt = target.getRuby() == null ? 0 : Integer.valueOf(target.getRuby());
            this.byeolCnt = target.getGold() == null ? 0 : Integer.valueOf(target.getGold());
        }else{
            this.exp = 0;
            this.expBegin = 0;
            this.expNext = 0;
            this.dalCnt = 0;
            this.byeolCnt = 0;
        }
        this.grade = target.getGrade();
        this.fanRank = fanRank;
        this.isRecomm = (target.getBadge_recomm() == 1) ? true : false;
        this.isPop = (target.getBadge_popular() == 1) ? true : false;
        this.isNew = (target.getBadge_newdj() == 1 ? true : false);
        this.broadTotTime = target.getBroadcastingTime();
        this.listenTotTime = target.getListeningTime();
        this.likeTotCnt = target.getReceivedGoodTotal();
    }
}
