package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageBlackAddVo;
import com.dalbit.member.vo.request.MypageBlackVo;
import com.dalbit.member.vo.request.MypageManagerAddVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MypageBlackAddVo {

    private String mem_no;
    private String black_mem_no;

    public P_MypageBlackAddVo(){}
    public P_MypageBlackAddVo(MypageBlackAddVo mypageBlackAddVo) {
        setMem_no(MemberVo.getMyMemNo());
        setBlack_mem_no(mypageBlackAddVo.getMemNo());
    }
}
