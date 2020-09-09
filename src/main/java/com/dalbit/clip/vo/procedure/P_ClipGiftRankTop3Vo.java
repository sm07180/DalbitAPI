package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipGiftRankTop3Vo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipGiftRankTop3Vo {

    public P_ClipGiftRankTop3Vo(){}
    public P_ClipGiftRankTop3Vo(ClipGiftRankTop3Vo clipGiftRankTop3Vo){
        setCast_no(clipGiftRankTop3Vo.getClipNo());
    }

    /* Input */
    private String cast_no;

    /* Output */
    private String mem_no;
    private String nickName;
    private String memSex;
    private int age;
    private String profileImage;

}
