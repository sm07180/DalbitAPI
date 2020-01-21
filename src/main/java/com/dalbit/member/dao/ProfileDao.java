package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.FanboardVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileDao {

    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    ProcedureVo callMemberFanboardAdd(ProcedureVo procedureVo);
    List<FanboardVo> callMemberFanboardList(ProcedureVo procedureVo);
    ProcedureVo callMemberFanboardDelete(ProcedureVo procedureVo);
    List<FanboardVo> callMemberFanboardReply(ProcedureVo procedureVo);

}
