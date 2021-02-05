package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.event.vo.procedure.P_OpenEventVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OpenEventOutVo {

    private int rank;
    private int level;
    private String[] levelColor;
    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private String gender;
    private String grade;
    private int totalPoint;

    public OpenEventOutVo(){}
    public OpenEventOutVo(P_OpenEventVo target){
        setRank(target.getRank());
        setLevel(target.getLevel());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setGender(target.getMemSex());
        setGrade(target.getGrade());
        setTotalPoint(target.getTotalPoint());
        int l = (target.getLevel() - 1) / 10;
        setLevelColor(DalbitUtil.getProperty("level.color." + l).split(","));
    }
}
