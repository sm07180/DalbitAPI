package com.dalbit.admin.vo.procedure;

import com.dalbit.admin.vo.AdminBaseVo;
import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberAdminMemoAddVo extends AdminBaseVo {
    private String mem_no;
    private String memo;
    private String opName;
}

