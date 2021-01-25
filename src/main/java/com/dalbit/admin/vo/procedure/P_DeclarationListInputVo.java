package com.dalbit.admin.vo.procedure;

import com.dalbit.admin.vo.AdminProcedureBaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_DeclarationListInputVo extends AdminProcedureBaseVo {
    private int pageNo;
    private String strPlatform;
    private String searchType;
    private String searchText;

    private int slctType;

    // 모바일 관리자 > 회원상세 > 신고 내역 조회 위해 추가
    private String memNo;
}
