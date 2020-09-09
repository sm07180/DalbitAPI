package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipEditVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipEditVo {

    private String mem_no;          //요청회원번호
    private String cast_no;         //클립 번호
    private String subjectType;     //클립 주제 (01~99)
    private String title;           //제목
    private int entryType;          //청취제한 (0: 전체, 1: 팬, 2: 20세이상)
    private int openType;           //공개여부(0: 비공개, 1: 공개)
    private String backgroundImage; //배경이미지

    public P_ClipEditVo(){}
    public P_ClipEditVo(ClipEditVo castEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(castEditVo.getClipNo());
        setSubjectType(castEditVo.getSubjectType());
        setTitle(castEditVo.getTitle());
        setEntryType(castEditVo.getEntryType());
        setOpenType(castEditVo.getOpenType());
        setBackgroundImage(castEditVo.getBgImg());
    }
    
}
