package com.dalbit.main.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainGoodRankingVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter @Setter
public class MainGoodRankingOutVo {

    private int rank;
    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private int level;
    private int fanCnt;
    private int totalGoodCnt;
    private int fanGoodCnt;
    private String fanMemNo;
    private String fanNickNm;
    private String holder;
    private String upDown;

    public MainGoodRankingOutVo(){}
    public MainGoodRankingOutVo(P_MainGoodRankingVo target) {
        setRank(target.getRank());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLevel(target.getLevel());
        setFanCnt(target.getFanCnt());
        setTotalGoodCnt(target.getTotal_goodCnt());
        setFanGoodCnt(target.getFan_goodCnt());
        setFanMemNo(target.getFan_mem_no());
        setFanNickNm(target.getFan_nickName());
        setHolder(StringUtils.replace(DalbitUtil.getProperty("level.frame"), "[level]", target.getLevel() + ""));
        setUpDown(target.getUpdown());
    }
}
