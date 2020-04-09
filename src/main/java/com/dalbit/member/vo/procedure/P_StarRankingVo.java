package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.request.StarRankingVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_StarRankingVo {
    private String selfMemNo;
    private String targetMemNo;

    private String memNo;
    private String nickNm;
    private String gender;
    private String profUrl;
    private int isFan = 0;

    public P_StarRankingVo(){}

    public P_StarRankingVo(StarRankingVo startRankigVo, String selfMemNo){
        this.selfMemNo = selfMemNo;
        this.targetMemNo = startRankigVo.getMemNo();
    }
}
