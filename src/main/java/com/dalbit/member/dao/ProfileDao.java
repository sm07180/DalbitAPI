package com.dalbit.member.dao;

import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.procedure.P_FanRankingVo;
import com.dalbit.member.vo.procedure.P_FanboardListVo;
import com.dalbit.member.vo.procedure.P_FanboardReplyVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileDao {

    ProcedureVo callMemberInfo(ProcedureVo procedureVo);
    ProcedureVo callMemberFanboardAdd(ProcedureVo procedureVo);
    List<P_FanboardListVo> callMemberFanboardList(ProcedureVo procedureVo);
    ProcedureVo callMemberFanboardDelete(ProcedureVo procedureVo);
    List<P_FanboardReplyVo> callMemberFanboardReply(ProcedureVo procedureVo);
    List<P_FanRankingVo> callMemberFanRanking(ProcedureVo procedureVo);
    ProcedureVo callMemberLevelUpCheck(ProcedureVo procedureVo);
}
