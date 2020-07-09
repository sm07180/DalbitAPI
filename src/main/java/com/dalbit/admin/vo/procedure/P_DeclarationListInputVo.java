package com.dalbit.admin.vo.procedure;

import com.dalbit.admin.vo.AdminProcedureBaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_DeclarationListInputVo extends AdminProcedureBaseVo {
    private int pageNo;
    private String strPlatform;

    private int slctType;
}
