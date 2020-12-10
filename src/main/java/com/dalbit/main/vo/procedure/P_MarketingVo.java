package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MarketingVo {
    private String idx;
    private String noticeIdx;

    private int slctType;
    private String title;
    private String round;

    private String memNo1;
    private String memSex1;
    private String memNick1;
    private String imageProfile1;
    private ImageVo imageInfo1;

    private String memNo2;
    private String memSex2;
    private String memNick2;
    private String imageProfile2;
    private ImageVo imageInfo2;

    private String regDate;

    private int level;
    private int likeCnt;
    private int listenCnt;
    private int airTime;
    private String holder;
    private String holderBg;
    private String[] levelColor;

    private int pageNo;
    private int pageCnt;

}
