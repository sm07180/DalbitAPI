package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.StarMemoSaveVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_StarMemoSaveVo {

    public P_StarMemoSaveVo(){}
    public P_StarMemoSaveVo(StarMemoSaveVo starMemoSaveVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setStar_mem_no(starMemoSaveVo.getMemNo());
        setMemo(starMemoSaveVo.getMemo());
    }


    /* Input */
    private String mem_no;
    private String star_mem_no;
    private String memo;
}
