package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.QnaVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_QnaVo {

    private String mem_no;
    private int slctType;
    private String title;
    private String contents;
    private String addFile;
    private String email;

    public P_QnaVo(){}
    public P_QnaVo(QnaVo qnaVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(qnaVo.getQnaType());
        setTitle(qnaVo.getTitle());
        setContents(qnaVo.getContents());
        setAddFile(qnaVo.getQuestionFile());
        setEmail(qnaVo.getEmail());
    }

}
