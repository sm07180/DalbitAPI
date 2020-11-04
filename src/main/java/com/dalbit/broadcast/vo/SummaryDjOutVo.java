package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_SummaryDjVo;
import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryDjOutVo {
    private String memNo;
    private String nickNm;
    private ImageVo profImg;
    private long gift;

    public SummaryDjOutVo(){}
    public SummaryDjOutVo(P_SummaryDjVo pSummaryDjVo, String svrPhotoUrl){
        this.memNo = pSummaryDjVo.getMem_no();
        this.nickNm = pSummaryDjVo.getMem_nick();
        this.profImg = new ImageVo(pSummaryDjVo.getImage_profile(), pSummaryDjVo.getMem_sex(), svrPhotoUrl);
        this.gift = pSummaryDjVo.getGold();
    }
}
