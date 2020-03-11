package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageManagerDelVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MypageManagerDelVo {

    private String mem_no;
    private String manager_mem_no;

    public P_MypageManagerDelVo(){}
    public P_MypageManagerDelVo(MypageManagerDelVo mypageManagerDelVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setManager_mem_no(mypageManagerDelVo.getMemNo());
    }
}
