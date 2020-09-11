package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipGiftVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipGiftVo {

    private String mem_no;
    private String cast_no;
    private String gifted_mem_no;
    private String item_code;
    private int item_cnt;

    public P_ClipGiftVo(){}
    public P_ClipGiftVo(ClipGiftVo clipGiftVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipGiftVo.getClipNo());
        setGifted_mem_no(clipGiftVo.getMemNo());
        setItem_code(clipGiftVo.getItemCode());
        setItem_cnt(clipGiftVo.getItemCnt());
    }
    
}