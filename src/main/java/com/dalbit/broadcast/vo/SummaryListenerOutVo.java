package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_SummaryListenerVo;
import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryListenerOutVo {
    private String roomNo;
    private String title;
    private int os;
    private String djNickNm;
    private ImageVo djProfImg;

    public SummaryListenerOutVo(){}
    public SummaryListenerOutVo(P_SummaryListenerVo pSummaryListenerVo, String svrPhotoUrl){
        this.roomNo = pSummaryListenerVo.getRoom_no();
        this.title = pSummaryListenerVo.getTitle();
        this.os = pSummaryListenerVo.getOs_type();
        this.djNickNm = pSummaryListenerVo.getMem_nick();
        this.djProfImg = new ImageVo(pSummaryListenerVo.getImage_profile(), pSummaryListenerVo.getMem_sex(), svrPhotoUrl);
    }
}
