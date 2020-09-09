package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipDeleteVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipDeleteVo {

    private String mem_no;          //요청회원번호
    private String cast_no;         //클립 번호

    public P_ClipDeleteVo(){}
    public P_ClipDeleteVo(ClipDeleteVo clipDeleteVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipDeleteVo.getClipNo());
    }
    
}
