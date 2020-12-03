package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RisingEventListOutputVo extends P_ApiVo {
    /* Output */
    private int rank;
    private String mem_no;
    private String profileImage;
    private String memSex;
    private String nickName;
    private int level;
    private int gainPoint;
    private int expPoint;
    private int listenerPoint;
    private String fanRank1;
    private String fanImage;
    private String fanSex;
    private String fanNick;
}
