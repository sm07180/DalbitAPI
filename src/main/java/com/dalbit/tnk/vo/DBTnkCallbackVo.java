package com.dalbit.tnk.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DBTnkCallbackVo {
    private String mem_no;
    private String seqId;
    private int payPoint;
    private String appId;
    private String appName;

    public DBTnkCallbackVo(TnkCallbackVo tnkCallbackVo){
        this.mem_no = tnkCallbackVo.getMd_user_nm();
        this.seqId = tnkCallbackVo.getSeq_id();
        this.payPoint = tnkCallbackVo.getPay_pnt();
        this.appId = tnkCallbackVo.getApp_id();
        this.appName = tnkCallbackVo.getApp_nm();
    }
}
