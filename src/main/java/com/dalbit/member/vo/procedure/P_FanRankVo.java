package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_FanRankVo extends P_ApiVo {
    private String mem_no;

    private String nickName;
    private String memSex;
    private int age;
    private String profileImage;

    private String bestDjMemNo;
}
