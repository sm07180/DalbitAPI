package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.P_BroadBasicVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MypageDao {
    ProcedureVo callProfileEdit(ProcedureVo procedureVo);
    ProcedureVo callFanstarInsert(ProcedureVo procedureVo);
    ProcedureVo callFanstarDelete(ProcedureVo procedureVo);
    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    List<P_BroadBasicVo> callBroadBasic(ProcedureVo procedureVo);
    ProcedureVo callBroadBasicEdit(ProcedureVo procedureVo);
    ProcedureVo callMemberReportAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockAdd(ProcedureVo procedureVo);
    ProcedureVo callMemberBlockDel(ProcedureVo procedureVo);
    ProcedureVo callMemberNotify(ProcedureVo procedureVo);
    ProcedureVo callMemberNotifyEdit(ProcedureVo procedureVo);
}
