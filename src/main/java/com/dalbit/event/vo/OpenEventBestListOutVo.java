package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.event.vo.procedure.P_OpenEventBestListVo;
import com.dalbit.event.vo.procedure.P_OpenEventVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter @Setter
public class OpenEventBestListOutVo {

    private String theDt;
    private long theTs;
    private int level;
    private String[] levelColor;
    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private String gender;
    private String grade;
    private Boolean isSpecial;
    private int badgeSpecial;
    private String holder;

    public OpenEventBestListOutVo(){}
    public OpenEventBestListOutVo(P_OpenEventBestListVo target){
        setTheDt(DalbitUtil.getUTCFormat(target.getThe_date()));
        setTheTs(DalbitUtil.getUTCTimeStamp(target.getThe_date()));
        setLevel(target.getLevel());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setGender(target.getMemSex());
        setGrade(target.getGrade());
        int l = (target.getLevel() - 1) / 10;
        setLevelColor(DalbitUtil.getProperty("level.color." + l).split(","));
        setIsSpecial(target.getSpecialdjBadge() > 0);
        setBadgeSpecial(target.getSpecialdjBadge());
        setHolder(StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", target.getLevel() + ""));
    }
}
