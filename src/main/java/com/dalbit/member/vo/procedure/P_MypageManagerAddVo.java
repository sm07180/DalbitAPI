package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageManagerAddVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MypageManagerAddVo extends P_ApiVo {

    private String mem_no;
    private String manager_mem_no;
    private String control_role;

    public P_MypageManagerAddVo(){}
    public P_MypageManagerAddVo(MypageManagerAddVo mypageManagerAddVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setManager_mem_no(mypageManagerAddVo.getMemNo());
        setControl_role(mypageManagerAddVo.getRole());
    }
}
