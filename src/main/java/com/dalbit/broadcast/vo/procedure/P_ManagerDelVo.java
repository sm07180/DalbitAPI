package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.ManagerDelVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ManagerDelVo {

    public P_ManagerDelVo(){}
    public P_ManagerDelVo(ManagerDelVo managerDelVo, HttpServletRequest request){
        setMem_no(new MemberVo().getMyMemNo(request));
        setRoom_no(managerDelVo.getRoomNo());
        setManager_mem_no(managerDelVo.getMemNo());
    }

    private String mem_no;
    private String room_no;
    private String manager_mem_no;
}
