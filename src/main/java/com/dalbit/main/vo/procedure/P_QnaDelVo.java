package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.QnaDelVo;
import com.dalbit.main.vo.request.QnaVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Getter @Setter
public class P_QnaDelVo {

    private String mem_no;
    private int qnaIdx;

    public P_QnaDelVo(){}
    public P_QnaDelVo(QnaDelVo qnaDelVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setQnaIdx(qnaDelVo.getQnaIdx());
    }

}
