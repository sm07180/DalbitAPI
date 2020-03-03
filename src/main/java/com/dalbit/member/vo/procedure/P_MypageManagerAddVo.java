package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageManagerAddVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MypageManagerAddVo {

    private String mem_no;
    private String manager_mem_no;
    private String control_role;

    public P_MypageManagerAddVo(){}
    public P_MypageManagerAddVo(MypageManagerAddVo mypageManagerAddVo) {
        setMem_no(MemberVo.getMyMemNo());
        setManager_mem_no(mypageManagerAddVo.getMemNo());
        setControl_role(mypageManagerAddVo.getRole());
    }
}
