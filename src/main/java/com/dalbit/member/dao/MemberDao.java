package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.procedure.P_ChangePasswordVo;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDao {

    ProcedureVo callNickNameCheck(ProcedureVo procedureVo);
    ProcedureVo callMemberLogin(ProcedureVo procedureVo);
    ProcedureVo callMemberJoin(ProcedureVo procedureVo);
    ProcedureVo callChangePassword(ProcedureVo procedureVo);
    ProcedureVo callMemberInfoView(ProcedureVo procedureVo);
    ProcedureVo callMemberSessionUpdate(ProcedureVo procedureVo);
}
