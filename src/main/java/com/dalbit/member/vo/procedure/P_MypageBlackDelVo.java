package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageBlackDelVo;
import com.dalbit.member.vo.request.MypageManagerDelVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MypageBlackDelVo extends P_ApiVo {

    private String mem_no;
    private String black_mem_no;

    public P_MypageBlackDelVo(){}
    public P_MypageBlackDelVo(MypageBlackDelVo mypageBlackDelVo, HttpServletRequest request) {
        setMem_no(new MemberVo().getMyMemNo(request));
        setBlack_mem_no(mypageBlackDelVo.getMemNo());
    }
}
