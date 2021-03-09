package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_ProfileImgListVo extends P_ApiVo {

    public P_ProfileImgListVo(){}
    public P_ProfileImgListVo(String memNo){
        setMem_no(memNo);
    }
    private String mem_no;

    private String profileImage;
    private int leader_yn;
    private int idx;
    private String memSex;

}
