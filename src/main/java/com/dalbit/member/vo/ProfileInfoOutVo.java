package com.dalbit.member.vo;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@ToString
public class ProfileInfoOutVo extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String  memNo;
    private String  teamNo;
    private String  nickNm;
    private String  gender;
    private int     age;
    private String  birth;
    private String  memId;
    private ImageVo profImg;
    private String  profMsg;
    private int     level = 0;
    private int     fanCnt;
    private int     starCnt;
    private Boolean isFan;
    private int     exp;
    private int     expBegin;
    private int     expNext;
    private int     expRate;
    private String  grade;
    private int     dalCnt;
    private int     byeolCnt;
    private Boolean isRecomm;
    private Boolean isPop;
    private Boolean isNew;
    private Boolean isSpecial;
    private int badgeSpecial;
    private String playMakerYn;
    private String roomNo;
    private long broadTotTime;
    private long listenTotTime;
    private int likeTotCnt;
    private String holder;
    private String holderBg;
    private String profileBg;
    List fanRank;
    private FanBadgeVo fanBadge;
    List fanBadgeList;
    List commonBadgeList;

    private String cupidMemNo;
    private String cupidNickNm;
    private ImageVo cupidProfImg;
    private boolean isNewListener = false;
    private HashMap count;

    List liveBadgeList;
    private int liveDjRank;
    private int liveFanRank;

    private boolean wasSpecial;
    private int specialDjCnt;
    private int memState;
    private String[] levelColor = new String[0];
    private String listenRoomNo;
    private boolean isReceive;
    List profImgList;
    private boolean isMailboxOn;
    private String memJoinYn;
    private int listenOpen;

    public ProfileInfoOutVo(){}
    public ProfileInfoOutVo(P_ProfileInfoVo target, String target_mem_no, String mem_no, List fanRank) {
        setProfileInfoOutVo(target, target_mem_no, mem_no, fanRank, false);
    }
    public ProfileInfoOutVo(P_ProfileInfoVo target, String target_mem_no, String mem_no, List fanRank, boolean isAdmin) {
        setProfileInfoOutVo(target, target_mem_no, mem_no, fanRank, isAdmin);
    }
    public void setProfileInfoOutVo(P_ProfileInfoVo target, String target_mem_no, String mem_no, List fanRank, boolean isAdmin) {
        this.memNo = target_mem_no;
        this.teamNo = target.getTeam_no();
        this.nickNm = target.getNickName();
        this.gender = target.getMemSex();
        this.age = target.getAge();
        this.memId = target.getMemId();
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.profMsg = target.getProfileMsg();
        this.level = target.getLevel();
        this.fanCnt = target.getFanCount();
        this.starCnt = target.getStarCount();
        this.isFan = (target.getEnableFan() == 0);
        this.roomNo = target.getRoom_no();
        this.holder = StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", this.level + "");
        int l = (this.level - 1) / 10;
        this.holderBg = DalbitUtil.getLevelFrameBg(this.level);
        this.levelColor = DalbitUtil.getProperty("level.color."+l).split(",");
        if(target_mem_no.equals(mem_no)){
            this.exp = target.getExp();
            this.expBegin = target.getExpBegin();
            this.expNext = target.getExpNext();
            this.expRate = DalbitUtil.getExpRate(target.getExp(), target.getExpBegin(), target.getExpNext());
            this.dalCnt = target.getRuby() == null ? 0 : Integer.valueOf(target.getRuby());
            this.byeolCnt = target.getGold() == null ? 0 : Integer.valueOf(target.getGold());
        }else{
            this.exp = 0;
            this.expBegin = 0;
            this.expNext = 0;
            this.expRate = 0;
            this.dalCnt = 0;
            this.byeolCnt = 0;
        }
        this.grade = target.getGrade();
        this.fanRank = fanRank;
        this.isRecomm = target.getBadge_recomm() == 1;
        this.isPop = target.getBadge_popular() == 1;
        this.isNew = target.getBadge_newdj() == 1;

        this.isNewListener = target.getBadge_new() == 1;
        this.isSpecial = (target.getBadge_specialdj() > 0);
        this.badgeSpecial = (target.getBadge_specialdj());
        this.playMakerYn = (target.getPlayMakerYn());
        this.broadTotTime = target.getBroadcastingTime();
        this.listenTotTime = target.getListeningTime();
        this.likeTotCnt = target.getReceivedGoodTotal();
        this.fanBadge = new FanBadgeVo(target.getFanBadgeText(), target.getFanBadgeIcon(), target.getFanBadgeStartColor(), target.getFanBadgeEndColor());
        this.cupidMemNo = target.getCupidMemNo();
        this.cupidNickNm = target.getCupidNickNm();
        this.cupidProfImg = new ImageVo(target.getCupidProfileImage(), target.getCupidMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.liveDjRank = target.getLiveDjRank() > 100 ? 0 : target.getLiveDjRank();
        this.liveFanRank = target.getLiveFanRank();

        this.specialDjCnt = target.getSpecialDjCnt();
        this.memState = target.getMemState();
        this.listenRoomNo = DalbitUtil.getListenRoomNo(target.getListenRoomNo(), target.getListenOpen(), isAdmin);
        this.isReceive = target.getAlertYn() == 1;
        this.isMailboxOn = target.getMailboxOnOff() == 1;
        this.memJoinYn = target.getMemJoinYn();
        this.listenOpen = target.getListenOpen();
    }
}
