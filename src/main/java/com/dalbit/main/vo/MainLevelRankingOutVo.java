package com.dalbit.main.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import com.dalbit.main.vo.procedure.P_MainLevelRankingVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter @Setter
public class MainLevelRankingOutVo {

    private int rank;
    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private int level;
    private String grade;
    private int fanCnt;
    private String fanMemNo;
    private String fanNickNm;
    private String roomNo;

    public MainLevelRankingOutVo(){}
    public MainLevelRankingOutVo(P_MainLevelRankingVo target) {
        setRank(target.getRank());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLevel(target.getLevel());
        setGrade(target.getGrade());
        setFanCnt(target.getFanCnt());
        setFanMemNo(target.getFan_mem_no());
        setFanNickNm(target.getNickName());
        setRoomNo(target.getRoomNo());
    }
}
