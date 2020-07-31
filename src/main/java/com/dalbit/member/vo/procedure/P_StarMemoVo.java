package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.StarMemoVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_StarMemoVo {

    public P_StarMemoVo(){}
    public P_StarMemoVo(StarMemoVo starMemoVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setStar_mem_no(starMemoVo.getMemNo());
    }


    /* Input */
    private String mem_no;
    private String star_mem_no;

    /* Output */
    private String starMemo;
}
