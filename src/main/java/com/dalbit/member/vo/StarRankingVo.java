package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_StarRankingVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StarRankingVo {
    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private boolean isFan = false;


    public StarRankingVo(){}
    public StarRankingVo(P_StarRankingVo pStarRankingVo, String photoSvrUrl){
        this.memNo = pStarRankingVo.getMemNo();
        this.nickNm = pStarRankingVo.getNickNm();
        this.isFan = pStarRankingVo.getIsFan() == 1;
        this.profImg = new ImageVo(pStarRankingVo.getProfUrl(), pStarRankingVo.getGender(), photoSvrUrl);
    }
}
