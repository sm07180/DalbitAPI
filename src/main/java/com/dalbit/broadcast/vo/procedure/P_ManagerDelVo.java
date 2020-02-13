package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.ManagerDelVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_ManagerDelVo {

    public P_ManagerDelVo(){}
    public P_ManagerDelVo(ManagerDelVo managerDelVo){
        setMem_no(MemberVo.getMyMemNo());
        setRoom_no(managerDelVo.getRoomNo());
        setManager_mem_no(managerDelVo.getMemNo());
    }

    private String mem_no;
    private String room_no;
    private String manager_mem_no;
}
