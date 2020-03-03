package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageBlackDelVo;
import com.dalbit.member.vo.request.MypageManagerDelVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MypageBlackDelVo {

    private String mem_no;
    private String black_mem_no;

    public P_MypageBlackDelVo(){}
    public P_MypageBlackDelVo(MypageBlackDelVo mypageBlackDelVo) {
        setMem_no(MemberVo.getMyMemNo());
        setBlack_mem_no(mypageBlackDelVo.getMemNo());
    }
}
