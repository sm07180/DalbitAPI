package com.demo.member.dao;

import com.demo.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDao {

    ProcedureVo callNickNameCheck(ProcedureVo procedureVo);
    ProcedureVo callMemberLogin(ProcedureVo procedureVo);
    ProcedureVo callMemberJoin(ProcedureVo procedureVo);
    ProcedureVo callChangePassword(ProcedureVo procedureVo);
    ProcedureVo callProfileEdit(ProcedureVo procedureVo);
}
