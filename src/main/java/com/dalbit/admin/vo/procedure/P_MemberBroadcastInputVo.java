package com.dalbit.admin.vo.procedure;

import com.dalbit.admin.vo.AdminProcedureBaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberBroadcastInputVo extends AdminProcedureBaseVo {
    private int pageNo;
    private String mem_no;
    private String room_no;
}
