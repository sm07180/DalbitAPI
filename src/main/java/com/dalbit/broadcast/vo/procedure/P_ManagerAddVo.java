package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.ManagerAddVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ManagerAddVo extends P_ApiVo {

    public P_ManagerAddVo(){}
    public P_ManagerAddVo(ManagerAddVo managerAddVo, HttpServletRequest request){
        setMem_no(new MemberVo().getMyMemNo(request));
        setRoom_no(managerAddVo.getRoomNo());
        setManager_mem_no(managerAddVo.getMemNo());
        setControl_role(managerAddVo.getRole());
        setManager_type(managerAddVo.getManagerType());
    }

    private String mem_no;
    private String room_no;
    private String manager_mem_no;
    private String control_role;
    private int manager_type;

}
