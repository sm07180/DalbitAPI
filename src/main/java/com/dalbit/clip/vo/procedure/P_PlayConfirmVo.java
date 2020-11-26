package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.PlayConfirmVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_PlayConfirmVo {

    public P_PlayConfirmVo(){}
    public P_PlayConfirmVo(PlayConfirmVo playConfirmVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(playConfirmVo.getClipNo());
        setPlayIdx(playConfirmVo.getPlayIdx());
    }

    private String mem_no;
    private String cast_no;
    private int playIdx;

}
