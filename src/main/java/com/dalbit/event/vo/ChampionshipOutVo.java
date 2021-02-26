package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.event.vo.procedure.P_ChampionshipVo;
import com.dalbit.event.vo.procedure.P_OpenEventVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChampionshipOutVo {

    private int rank;
    private int level;
    private String[] levelColor;
    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private String gender;
    private String grade;
    private int totalPoint;
    private String qupidMemNo;
    private String qupidNickNm;
    private ImageVo qupidProfImg;
    private String qupidGender;

    public ChampionshipOutVo(){}
    public ChampionshipOutVo(P_ChampionshipVo target){
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
        setQupidMemNo(target.getQupid_mem_no());
        setQupidNickNm(target.getQupid_memNick());
        setQupidProfImg(new ImageVo(target.getQupid_profileImage(), target.getQupid_memSex(), DalbitUtil.getProperty("server.photo.url")));
        setQupidGender(target.getQupid_memSex());
    }
}
