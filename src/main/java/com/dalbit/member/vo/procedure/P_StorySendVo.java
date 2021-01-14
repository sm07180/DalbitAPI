package com.dalbit.member.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_StorySendVo {
    /* Input */
    private String mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int storyIdx;
    private String roomNo;
    private String title;
    private String djMemNo;
    private String djNickNm;
    private String djGender;
    private String djProfile;
    private String contents;
    private String writeDt;
}
