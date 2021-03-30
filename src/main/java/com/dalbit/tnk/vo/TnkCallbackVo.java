package com.dalbit.tnk.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TnkCallbackVo {
    private String seq_id;
    private int pay_pnt;
    private String md_user_nm;
    private String md_chk;
    private String app_id;
    private long pay_dt;
    private String app_nm;
}
