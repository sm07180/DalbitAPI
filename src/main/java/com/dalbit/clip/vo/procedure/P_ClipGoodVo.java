package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipGoodVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipGoodVo {

    private String mem_no;
    private String cast_no;
    private int good;

    public P_ClipGoodVo(){}
    public P_ClipGoodVo(ClipGoodVo clipGoodVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipGoodVo.getClipNo());
        setGood(clipGoodVo.getGood());
    }
    
}
