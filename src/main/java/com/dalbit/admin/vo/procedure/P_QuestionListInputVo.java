package com.dalbit.admin.vo.procedure;

import com.dalbit.admin.vo.AdminProcedureBaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_QuestionListInputVo extends AdminProcedureBaseVo {

    private String slctPlatform;
    private String slctBrowser;
    private int sortPlatform;
    private int sortBrowser;
    private int sortState;
    private int pageNo;
    private int slctState;

    private String mem_no;
    
}
