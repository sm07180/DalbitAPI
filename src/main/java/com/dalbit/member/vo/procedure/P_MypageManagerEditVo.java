package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageManagerEditVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MypageManagerEditVo {

    private String mem_no;
    private String manager_mem_no;
    private String control_role;

    public P_MypageManagerEditVo(){}
    public P_MypageManagerEditVo(MypageManagerEditVo mypageManagerEditVo) {
        setMem_no(MemberVo.getMyMemNo());
        setManager_mem_no(mypageManagerEditVo.getMemNo());
        setControl_role(mypageManagerEditVo.getRole());
    }
}
