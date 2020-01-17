package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileDao {

    ProcedureVo callMemberFanboardAdd(ProcedureVo procedureVo);


    ProcedureVo callFanboardAdd(ProcedureVo procedureVo);
    ProcedureVo callFanboardList(ProcedureVo procedureVo);
    ProcedureVo callFanboardDelete(ProcedureVo procedureVo);
    ProcedureVo callFanboardReply(ProcedureVo procedureVo);

}
