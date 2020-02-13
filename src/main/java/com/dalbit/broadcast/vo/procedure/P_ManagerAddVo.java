package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.ManagerAddVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_ManagerAddVo {

    public P_ManagerAddVo(){}
    public P_ManagerAddVo(ManagerAddVo managerAddVo){
        setMem_no(MemberVo.getMyMemNo());
        setRoom_no(managerAddVo.getRoomNo());
        setManager_mem_no(managerAddVo.getMemNo());
        setControl_role(managerAddVo.getRole());
    }

    private String mem_no;
    private String room_no;
    private String manager_mem_no;
    private String control_role;

}
