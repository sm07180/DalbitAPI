package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class P_MarketingVo extends P_ApiVo {
    private String idx;
    private String noticeIdx;

    private int slctType;
    private String title;
    private String round;

    private ArrayList memberList;

    private transient String memNo1;
    private transient String memSex1;
    private transient String memNick1;
    private transient String imageProfile1;
    private transient ImageVo imageInfo1;

    private transient String memNo2;
    private transient String memSex2;
    private transient String memNick2;
    private transient String imageProfile2;
    private transient ImageVo imageInfo2;

    private String regDate;
    private transient int level;
    private int likeCnt;
    private int listenCnt;
    private int airTime;
    private transient String holder;
    private transient String holderBg;
    private transient String[] levelColor;

    private int pageNo;
    private int pageCnt;

}
